#!/usr/bin/env python3
"""本地网关压测工具（第一轮）。

只打本地网关，不产生任何外部付费流量。

用法:
    python bench.py                                   # 20 并发 / 200 请求 / 非流式
    python bench.py -c 50 -n 500 --stream             # 50 并发 / 500 请求 / 流式
    python bench.py -c 32 -n 320 --interval-ms 50     # 按 50ms 间隔匀速注入
    python bench.py -c 8 -n 40 --stream --report artifacts/bench_stream.json
"""
from __future__ import annotations

import argparse
import asyncio
import json
import os
import statistics
import sys
import time

import httpx

BASE = os.getenv("GATEWAY_BASE", "http://127.0.0.1:8960")
PAYLOAD = {
    "model": "gpt-4o-mini",
    "temperature": 0.7,
    "max_tokens": 256,
    "messages": [
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": "用两三句话解释一下什么是 SSE。"},
    ],
}


def pct(values: list[float], q: float) -> float:
    if not values:
        return 0.0
    ordered = sorted(values)
    idx = min(len(ordered) - 1, max(0, int(round(q * (len(ordered) - 1)))))
    return ordered[idx]


async def one(client: httpx.AsyncClient, stream: bool, sem: asyncio.Semaphore, out: dict) -> None:
    async with sem:
        payload = dict(PAYLOAD, stream=stream)
        started = time.perf_counter()
        ttft = None
        chars = 0
        status = 0
        try:
            if not stream:
                r = await client.post("/v1/chat/completions", json=payload)
                status = r.status_code
                body = r.json()
                if status == 200:
                    chars = len(body["choices"][0]["message"]["content"])
                else:
                    out["errors"][f"http_{status}"] = out["errors"].get(f"http_{status}", 0) + 1
            else:
                async with client.stream("POST", "/v1/chat/completions", json=payload) as r:
                    status = r.status_code
                    if status != 200:
                        out["errors"][f"http_{status}"] = out["errors"].get(f"http_{status}", 0) + 1
                        await r.aread()
                    else:
                        async for line in r.aiter_lines():
                            if not line.startswith("data:"):
                                continue
                            data = line[5:].strip()
                            if data == "[DONE]":
                                break
                            try:
                                obj = json.loads(data)
                            except json.JSONDecodeError:
                                out["errors"]["bad_sse"] = out["errors"].get("bad_sse", 0) + 1
                                continue
                            if "error" in obj:
                                out["errors"]["sse_error"] = out["errors"].get("sse_error", 0) + 1
                                break
                            delta = obj["choices"][0]["delta"].get("content") or ""
                            if delta:
                                if ttft is None:
                                    ttft = time.perf_counter() - started
                                chars += len(delta)
        except Exception as exc:  # noqa: BLE001 - 压测工具需要统计一切失败
            out["errors"][type(exc).__name__] = out["errors"].get(type(exc).__name__, 0) + 1
        elapsed = (time.perf_counter() - started) * 1000
        out["latency"].append(elapsed)
        if ttft is not None:
            out["ttft"].append(ttft * 1000)
        out["chars"] += chars
        out["statuses"][str(status)] = out["statuses"].get(str(status), 0) + 1


async def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("-c", "--concurrency", type=int, default=20)
    ap.add_argument("-n", "--requests", type=int, default=200)
    ap.add_argument("--stream", action="store_true")
    ap.add_argument("--interval-ms", type=float, default=0.0, help="相邻请求注入间隔")
    ap.add_argument("--report", help="把结果写入 JSON")
    ap.add_argument("--timeout", type=float, default=120.0)
    args = ap.parse_args()

    limits = httpx.Limits(max_connections=args.concurrency + 8, max_keepalive_connections=args.concurrency + 8)
    sem = asyncio.Semaphore(args.concurrency)
    out = {"latency": [], "ttft": [], "errors": {}, "statuses": {}, "chars": 0}

    async with httpx.AsyncClient(base_url=BASE, timeout=args.timeout, limits=limits) as client:
        if (await client.get("/healthz")).status_code != 200:
            print("gateway not healthy", file=sys.stderr)
            return 2
        print(f"target={BASE} stream={args.stream} concurrency={args.concurrency} requests={args.requests}")
        started = time.perf_counter()
        tasks = []
        for i in range(args.requests):
            tasks.append(asyncio.create_task(one(client, args.stream, sem, out)))
            if args.interval_ms:
                await asyncio.sleep(args.interval_ms / 1000)
        await asyncio.gather(*tasks)
        wall = time.perf_counter() - started

    lat, ttft = out["latency"], out["ttft"]
    ok = out["statuses"].get("200", 0)
    summary = {
        "mode": "stream" if args.stream else "blocking",
        "concurrency": args.concurrency,
        "requests": args.requests,
        "ok": ok,
        "failed": args.requests - ok,
        "wall_s": round(wall, 2),
        "qps": round(args.requests / wall, 1),
        "latency_ms": {
            "mean": round(statistics.fmean(lat), 1) if lat else 0,
            "p50": round(pct(lat, 0.50), 1),
            "p90": round(pct(lat, 0.90), 1),
            "p99": round(pct(lat, 0.99), 1),
            "max": round(max(lat), 1) if lat else 0,
        },
        "ttft_ms": {
            "p50": round(pct(ttft, 0.50), 1),
            "p90": round(pct(ttft, 0.90), 1),
            "p99": round(pct(ttft, 0.99), 1),
        } if ttft else None,
        "chars": out["chars"],
        "statuses": out["statuses"],
        "errors": out["errors"],
    }
    print(json.dumps(summary, ensure_ascii=False, indent=2))
    if args.report:
        os.makedirs(os.path.dirname(args.report) or ".", exist_ok=True)
        with open(args.report, "w", encoding="utf8") as fh:
            json.dump(summary, fh, ensure_ascii=False, indent=2)
        print(f"written -> {args.report}")
    return 0 if summary["failed"] == 0 else 1


if __name__ == "__main__":
    raise SystemExit(asyncio.run(main()))
