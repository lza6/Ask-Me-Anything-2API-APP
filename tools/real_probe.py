#!/usr/bin/env python3
"""真实上游连通性探针：按还原后的协议直接打 nowai 入口，打印服务端原始响应。

用途：一旦上游复活（或指向自建同协议服务），用这个脚本一键判定协议是否仍然有效，
而不是靠猜。默认目标为 App 内置入口，可用 --url 覆盖。

用法:
    python tools/real_probe.py                       # 打 App 内置 pass 入口
    python tools/real_probe.py --url http://127.0.0.1:9000/api/v1/pass
    python tools/real_probe.py --stream              # 观察 SSE 分片
"""
from __future__ import annotations

import argparse
import asyncio
import json
import os
import sys

import httpx

sys.path.insert(0, os.path.dirname(__file__))
from sign import sign  # noqa: E402

DEFAULT_URL = os.getenv("NOWAI_PASS_URL", "http://pass-gpt.nowtechai.com/api/v1/pass")


async def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--url", default=DEFAULT_URL)
    ap.add_argument("--prompt", default="用一句话介绍你自己")
    ap.add_argument("--model-type", default=None, help="仅用于记录（v1.7.0 请求体无 model 字段）")
    ap.add_argument("--stream", action="store_true")
    ap.add_argument("--timeout", type=float, default=30.0)
    args = ap.parse_args()

    key, ts = sign()
    body = {
        "contents": [
            {"role": "system", "content": "You are a helpful assistant."},
            {"role": "user", "content": args.prompt},
        ]
    }
    headers = {
        "Key": key,
        "TimeStamps": str(ts),
        "Content-Type": "application/json",
        "Accept": "text/event-stream",
        "User-Agent": "okhttp/4.12.0",
    }

    print(f"POST {args.url}")
    print(f"Key        = {key[:32]}...(len {len(key)})")
    print(f"TimeStamps = {ts}")
    print(f"body       = {json.dumps(body, ensure_ascii=False)}")
    print("-" * 60)

    async with httpx.AsyncClient(timeout=httpx.Timeout(args.timeout, connect=10.0)) as client:
        try:
            if not args.stream:
                r = await client.post(args.url, headers=headers, json=body)
                print(f"status = {r.status_code}")
                print(f"headers = {dict(r.headers)}")
                print(f"body   = {r.text[:800]}")
                return 0 if r.status_code == 200 else 1

            async with client.stream("POST", args.url, headers=headers, json=body) as r:
                print(f"status = {r.status_code}")
                if r.status_code != 200:
                    print(f"body   = {(await r.aread())[:500]!r}")
                    return 1
                lines = 0
                async for line in r.aiter_lines():
                    if not line.strip():
                        continue
                    lines += 1
                    print(f"[{lines:03d}] {line[:200]}")
                    if lines >= 40:
                        print("... (截断)")
                        break
                return 0
        except httpx.TimeoutException:
            print(f"TIMEOUT after {args.timeout}s —— 上游不可达")
            return 2
        except httpx.HTTPError as exc:
            print(f"NETWORK ERROR: {type(exc).__name__}: {exc}")
            return 2


if __name__ == "__main__":
    raise SystemExit(asyncio.run(main()))
