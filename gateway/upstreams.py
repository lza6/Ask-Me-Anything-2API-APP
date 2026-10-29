"""上游适配器：mock（本地零成本）与 nowai（逆向还原的自建网关协议）。

nowai 协议的完整链路（还原自 com.android.ntduc.chatgpt.data.DataRepository）：

    Key, ts = base64(HMAC_SHA512(secret, str(ts))), int(time()*1000)
    POST {PASS_URL}                       # http://pass-gpt.nowtechai.com/api/v1/pass
      Key: <Key>
      TimeStamps: <ts>
      Content-Type: application/json
      {"contents":[{"role":"system","content":...},{"role":"user","content":...}]}
    <- text/event-stream，每行 {"content":"...","status":"..."}
"""
from __future__ import annotations

import asyncio
import json
import os
import sys
from typing import AsyncIterator

import httpx

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "tools"))
from sign import sign  # noqa: E402


class UpstreamError(Exception):
    """kind: timeout | rate_limit | provider_error | bad_response | not_configured"""

    def __init__(self, kind: str, detail: str):
        super().__init__(detail)
        self.kind = kind
        self.detail = detail


class Upstream:
    name = "base"

    async def astream(self, messages: list[dict], temperature: float, max_tokens: int) -> AsyncIterator[str]:
        raise NotImplementedError
        yield ""  # pragma: no cover


# --------------------------------------------------------------------------- mock
MOCK_REPLY = (
    "[mock-upstream] 这是一条本地生成的回复，用于在真实上游不可达时验证网关链路与前端演示。"
    "当前请求包含 {n} 条消息，最后一条用户输入为：{last}"
)


class MockUpstream(Upstream):
    name = "mock"

    def __init__(self) -> None:
        # 单位为毫秒，用于压测时模拟真实首 token 与 chunk 间隔
        self.first_token_ms = int(os.getenv("MOCK_FIRST_TOKEN_MS", "0"))
        self.chunk_ms = int(os.getenv("MOCK_CHUNK_MS", "0"))
        # 故障注入：none | timeout | rate_limit | provider_error | malformed
        self.fail_kind = os.getenv("MOCK_FAIL_KIND", "none").strip().lower()

    async def astream(self, messages: list[dict], temperature: float, max_tokens: int) -> AsyncIterator[str]:
        if self.fail_kind == "timeout":
            await asyncio.sleep(float(os.getenv("MOCK_FAIL_DELAY_S", "30")))
            raise UpstreamError("timeout", "mock upstream timeout")
        if self.fail_kind == "rate_limit":
            raise UpstreamError("rate_limit", "mock upstream rate limit")
        if self.fail_kind == "provider_error":
            raise UpstreamError("provider_error", "mock upstream 500")
        if self.fail_kind == "malformed":
            raise UpstreamError("bad_response", "mock upstream returned non-json body")

        last = ""
        for msg in reversed(messages):
            if msg.get("role") == "user":
                last = str(msg.get("content", ""))[:80]
                break
        text = MOCK_REPLY.format(n=len(messages), last=last or "(empty)")
        if self.first_token_ms:
            await asyncio.sleep(self.first_token_ms / 1000)

        step = 8
        for i in range(0, len(text), step):
            yield text[i : i + step]
            if self.chunk_ms:
                await asyncio.sleep(self.chunk_ms / 1000)


# -------------------------------------------------------------------------- nowai
class NowAiUpstream(Upstream):
    name = "nowai"

    def __init__(self) -> None:
        self.pass_url = os.getenv("NOWAI_PASS_URL", "http://pass-gpt.nowtechai.com/api/v1/pass")
        self.secret = os.getenv("NOWAI_HMAC_SECRET", "").strip() or None
        self.timeout = float(os.getenv("NOWAI_TIMEOUT_S", "60"))
        self._client = httpx.AsyncClient(timeout=httpx.Timeout(self.timeout, connect=10.0))

    async def astream(self, messages: list[dict], temperature: float, max_tokens: int) -> AsyncIterator[str]:
        key, ts = sign(secret=self.secret)
        contents = [
            {"role": str(m.get("role", "user")), "content": str(m.get("content", ""))}
            for m in messages
            if m.get("role") in {"system", "user", "assistant"}
        ]
        try:
            async with self._client.stream(
                "POST",
                self.pass_url,
                headers={
                    "Key": key,
                    "TimeStamps": str(ts),
                    "Content-Type": "application/json",
                    "Accept": "text/event-stream",
                    "User-Agent": "okhttp/4.12.0",
                },
                json={"contents": contents},
            ) as resp:
                if resp.status_code == 429:
                    raise UpstreamError("rate_limit", f"upstream 429: {(await resp.aread())[:200]!r}")
                if resp.status_code >= 400:
                    raise UpstreamError("provider_error", f"upstream {resp.status_code}: {(await resp.aread())[:200]!r}")

                async for raw in resp.aiter_lines():
                    line = raw.strip()
                    if not line:
                        continue
                    if line.startswith("data:"):
                        line = line[5:].strip()
                    if not line or line == "[DONE]":
                        continue
                    try:
                        obj = json.loads(line)
                    except json.JSONDecodeError as exc:
                        raise UpstreamError("bad_response", f"non-json sse line: {line[:120]!r}") from exc
                    if isinstance(obj, dict) and obj.get("detail"):
                        raise UpstreamError("provider_error", str(obj["detail"])[:200])
                    piece = obj.get("content") if isinstance(obj, dict) else None
                    if piece:
                        yield str(piece)
                    if isinstance(obj, dict) and str(obj.get("status", "")).lower() in {"done", "stop", "finish", "end"}:
                        return
        except httpx.TimeoutException as exc:
            raise UpstreamError("timeout", f"upstream timeout after {self.timeout}s") from exc
        except httpx.HTTPError as exc:
            raise UpstreamError("provider_error", f"{type(exc).__name__}: {exc}") from exc


def build_upstream() -> Upstream:
    mode = os.getenv("UPSTREAM", "mock").strip().lower()
    if mode == "nowai":
        return NowAiUpstream()
    if mode == "mock":
        return MockUpstream()
    raise SystemExit(f"unknown UPSTREAM={mode!r} (expected mock|nowai)")
