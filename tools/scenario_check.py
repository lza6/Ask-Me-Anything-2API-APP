#!/usr/bin/env python3
"""上游故障场景矩阵验证：逐个启动网关子进程并断言 HTTP 状态码与错误码映射。

覆盖 CLAUDE.md 要求的 mock 场景：success / rate_limit / provider_error /
malformed_response / timeout / cancellation(客户端断开) / 闸门背压。

用法:
    python tools/scenario_check.py            # 全部按序跑，输出 PASS/FAIL 表
"""
from __future__ import annotations

import asyncio
import os
import socket
import subprocess
import sys
import time

import httpx

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PY = sys.executable

BODY = {
    "model": "gpt-4o-mini",
    "messages": [{"role": "user", "content": "ping"}],
    "max_tokens": 32,
}


def free_port() -> int:
    with socket.socket() as s:
        s.bind(("127.0.0.1", 0))
        return s.getsockname()[1]


async def wait_health(base: str, timeout: float = 25.0) -> bool:
    deadline = time.time() + timeout
    async with httpx.AsyncClient(base_url=base, timeout=2.0) as c:
        while time.time() < deadline:
            try:
                if (await c.get("/healthz")).status_code == 200:
                    return True
            except Exception:  # noqa: BLE001
                pass
            await asyncio.sleep(0.3)
    return False


async def run_case(name: str, env_extra: dict[str, str], expect_status: int, expect_code: str | None,
                   queue_burst: int = 1) -> tuple[str, bool, str]:
    port = free_port()
    base = f"http://127.0.0.1:{port}"
    env = dict(os.environ, PYTHONIOENCODING="utf-8", **env_extra)
    proc = subprocess.Popen(
        [PY, "-m", "uvicorn", "gateway.app:app", "--host", "127.0.0.1", "--port", str(port), "--log-level", "error"],
        cwd=ROOT, env=env, stdout=subprocess.DEVNULL, stderr=subprocess.PIPE,
    )
    try:
        if not await wait_health(base):
            return name, False, "gateway 未启动: " + (proc.stderr.read(400).decode("utf8", "replace") if proc.stderr else "")
        async with httpx.AsyncClient(base_url=base, timeout=60.0) as client:
            if queue_burst > 1:
                rs = await asyncio.gather(*[client.post("/v1/chat/completions", json=BODY) for _ in range(queue_burst)])
                codes = [r.status_code for r in rs]
                ok = expect_status in codes
                detail = f"statuses={codes}"
                if ok:
                    hit = next(r for r in rs if r.status_code == expect_status)
                    body = hit.json()
                    code = body.get("error", {}).get("code")
                    ok = expect_code is None or code == expect_code
                    detail += f" code={code}"
                return name, ok, detail

            r = await client.post("/v1/chat/completions", json=BODY)
            body = r.json()
            code = body.get("error", {}).get("code")
            ok = r.status_code == expect_status and (expect_code is None or code == expect_code)
            return name, ok, f"status={r.status_code} code={code} msg={str(body)[:120]}"
    finally:
        proc.terminate()
        try:
            proc.wait(timeout=10)
        except subprocess.TimeoutExpired:
            proc.kill()


async def cancellation_case() -> tuple[str, bool, str]:
    """客户端在流式过程中主动断开，验证信号量被回收（无泄漏）。"""
    port = free_port()
    base = f"http://127.0.0.1:{port}"
    env = dict(os.environ, PYTHONIOENCODING="utf-8", UPSTREAM="mock", MOCK_CHUNK_MS="40", MAX_CONCURRENCY="2")
    proc = subprocess.Popen(
        [PY, "-m", "uvicorn", "gateway.app:app", "--host", "127.0.0.1", "--port", str(port), "--log-level", "error"],
        cwd=ROOT, env=env, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL,
    )
    try:
        if not await wait_health(base):
            return "cancellation", False, "gateway 未启动"
        async with httpx.AsyncClient(base_url=base, timeout=30.0) as client:
            for _ in range(6):  # 6 次「读几帧就跑」，超过闸门 2 也应当不堵塞
                async with client.stream("POST", "/v1/chat/completions", json=dict(BODY, stream=True)) as r:
                    assert r.status_code == 200
                    got = 0
                    async for _line in r.aiter_lines():
                        got += 1
                        if got >= 3:
                            break
                await asyncio.sleep(0.05)
            h = (await client.get("/healthz")).json()
            ok = h["in_flight"] == 0
            return "cancellation", ok, f"in_flight={h['in_flight']} (期望 0，泄漏即信号量卡死)"
    finally:
        proc.terminate()
        try:
            proc.wait(timeout=10)
        except subprocess.TimeoutExpired:
            proc.kill()


CASES = [
    ("success", {"UPSTREAM": "mock"}, 200, None, 1),
    ("rate_limit", {"UPSTREAM": "mock", "MOCK_FAIL_KIND": "rate_limit"}, 429, "rate_limit", 1),
    ("provider_error", {"UPSTREAM": "mock", "MOCK_FAIL_KIND": "provider_error"}, 502, "provider_error", 1),
    ("malformed_response", {"UPSTREAM": "mock", "MOCK_FAIL_KIND": "malformed"}, 502, "bad_response", 1),
    ("network_refused", {"UPSTREAM": "nowai", "NOWAI_PASS_URL": "http://127.0.0.1:9/api/v1/pass"}, 502, "provider_error", 1),
    ("timeout", {"UPSTREAM": "nowai", "NOWAI_PASS_URL": "http://10.255.255.1:83/api/v1/pass", "NOWAI_TIMEOUT_S": "3"}, 504, "timeout", 1),
    ("gate_busy", {"UPSTREAM": "mock", "MOCK_FIRST_TOKEN_MS": "1500", "MAX_CONCURRENCY": "1", "QUEUE_TIMEOUT_S": "0.2"}, 429, "gateway_busy", 4),
]


async def main() -> int:
    results = []
    for name, env, status, code, burst in CASES:
        results.append(await run_case(name, env, status, code, burst))
    results.append(await cancellation_case())

    width = max(len(r[0]) for r in results)
    failed = 0
    for name, ok, detail in results:
        failed += 0 if ok else 1
        print(f"{'PASS' if ok else 'FAIL'}  {name:<{width}}  {detail}")
    print(f"\n{len(results) - failed}/{len(results)} passed")
    return 0 if failed == 0 else 1


if __name__ == "__main__":
    raise SystemExit(asyncio.run(main()))
