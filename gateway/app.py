#!/usr/bin/env python3
"""Ask Me Anything v1.7.0 —— 本地 OpenAI 兼容网关（第一轮 demo）。

上游现状见 docs/UPSTREAM_STATUS.md：App 真实服务端已全线不可用，因此默认
UPSTREAM=mock 跑通完整链路（前端 -> 网关 -> 适配器 -> 流式响应），
UPSTREAM=nowai 可在上游复活或指到自建同协议服务时一键切回真实协议。

启动:
    python -m uvicorn gateway.app:app --host 127.0.0.1 --port 8960
    # 打开 http://127.0.0.1:8960/ 即为 demo 页
"""
from __future__ import annotations

import asyncio
import json
import os
import sys
import time
import uuid
from contextlib import asynccontextmanager
from typing import Any, Literal

from fastapi import FastAPI, Request
from fastapi.responses import FileResponse, JSONResponse, StreamingResponse
from starlette.background import BackgroundTask
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel, Field, field_validator

sys.path.insert(0, os.path.dirname(__file__))
from upstreams import UpstreamError, build_upstream  # noqa: E402

MAX_TOKENS_LIMIT = 8192          # 与 new-api 的 maxTokensLimit 对齐，防止无界计费量
MAX_MESSAGES = 100
MAX_CONTENT_CHARS = 32_000
MAX_CONCURRENCY = int(os.getenv("MAX_CONCURRENCY", "32"))
QUEUE_TIMEOUT_S = float(os.getenv("QUEUE_TIMEOUT_S", "20"))

MODEL_ALIASES = {
    # App 内 config_chat_model 的 type 编码 -> 对外模型名
    "gpt-4o-mini": "102",
    "gpt-4o": "103",
    "o1": "104",
    "claude": "105",
    "gemini-3.0-pro": "106",
    "deepseek-r1": "107",
    "grok": "108",
    "llama-4": "109",
}
SERVED_MODELS = list(MODEL_ALIASES)

METRICS = {"requests": 0, "errors": 0, "in_flight": 0, "upstream_errors": {}}
_sem = asyncio.Semaphore(MAX_CONCURRENCY)
_upstream = None


@asynccontextmanager
async def lifespan(_: FastAPI):
    global _upstream
    _upstream = build_upstream()
    print(f"[gateway] upstream={_upstream.name} max_concurrency={MAX_CONCURRENCY}", flush=True)
    yield


app = FastAPI(title="Ask Me Anything 2API gateway", version="0.1.0", lifespan=lifespan)


class ChatMessage(BaseModel):
    role: Literal["system", "user", "assistant"]
    content: str = Field(min_length=0, max_length=MAX_CONTENT_CHARS)


class ChatCompletionRequest(BaseModel):
    model: str = "gpt-4o-mini"
    messages: list[ChatMessage] = Field(min_length=1, max_length=MAX_MESSAGES)
    temperature: float = Field(default=0.7, ge=0.0, le=2.0)
    max_tokens: int = Field(default=1024, ge=1, le=MAX_TOKENS_LIMIT)
    stream: bool = False

    @field_validator("model")
    @classmethod
    def _known_model(cls, v: str) -> str:
        if v not in MODEL_ALIASES:
            raise ValueError(f"unknown model {v!r}; served: {', '.join(SERVED_MODELS)}")
        return v


def _error(status: int, message: str, type_: str, code: str) -> JSONResponse:
    METRICS["errors"] += 1
    return JSONResponse(status_code=status, content={"error": {"message": message, "type": type_, "code": code}})


def _usage(messages: list[ChatMessage], answer: str) -> dict[str, int]:
    """粗估 token：CJK 按 1 字 1 token，其余按 4 字符 1 token。仅用于演示计费形状。"""
    def approx(text: str) -> int:
        if not text:
            return 0
        cjk = sum(1 for ch in text if "一" <= ch <= "鿿")
        return cjk + (len(text) - cjk + 3) // 4

    prompt = sum(approx(m.content) for m in messages)
    completion = approx(answer)
    return {"prompt_tokens": prompt, "completion_tokens": completion, "total_tokens": prompt + completion}


@app.get("/healthz")
async def healthz() -> dict[str, Any]:
    return {"ok": True, "upstream": _upstream.name if _upstream else None, "in_flight": METRICS["in_flight"]}


@app.get("/metrics")
async def metrics() -> dict[str, Any]:
    return dict(METRICS, max_concurrency=MAX_CONCURRENCY)


@app.get("/v1/models")
async def models() -> dict[str, Any]:
    now = int(time.time())
    return {
        "object": "list",
        "data": [{"id": m, "object": "model", "created": now, "owned_by": "nowai"} for m in SERVED_MODELS],
    }


class _Gate:
    """幂等释放的并发闸门令牌，防止「客户端提前断开导致生成器未消费」泄漏信号量。"""

    __slots__ = ("_released",)

    def __init__(self) -> None:
        self._released = False

    async def release(self) -> None:
        if self._released:
            return
        self._released = True
        METRICS["in_flight"] -= 1
        _sem.release()


@app.post("/v1/chat/completions")
async def chat_completions(req: ChatCompletionRequest, request: Request) -> Any:
    METRICS["requests"] += 1
    try:
        await asyncio.wait_for(_sem.acquire(), timeout=QUEUE_TIMEOUT_S)
    except asyncio.TimeoutError:
        return _error(429, "gateway busy: concurrency gate full", "rate_limit_error", "gateway_busy")

    METRICS["in_flight"] += 1
    gate = _Gate()
    completion_id = f"chatcmpl-{uuid.uuid4().hex[:24]}"
    created = int(time.time())
    msgs = [m.model_dump() for m in req.messages]

    if req.stream:
        return StreamingResponse(
            _stream_body(gate, completion_id, created, req, msgs, request),
            media_type="text/event-stream",
            headers={"Cache-Control": "no-cache", "X-Accel-Buffering": "no"},
            background=BackgroundTask(gate.release),
        )
    return await _blocking_body(gate, completion_id, created, req, msgs)


async def _blocking_body(gate: _Gate, completion_id: str, created: int, req: ChatCompletionRequest, msgs: list[dict]) -> Any:
    chunks: list[str] = []
    try:
        async for piece in _upstream.astream(msgs, req.temperature, req.max_tokens):
            chunks.append(piece)
    except UpstreamError as exc:
        METRICS["upstream_errors"][exc.kind] = METRICS["upstream_errors"].get(exc.kind, 0) + 1
        status = 504 if exc.kind == "timeout" else 429 if exc.kind == "rate_limit" else 502
        return _error(status, exc.detail, f"upstream_{exc.kind}", exc.kind)
    finally:
        await gate.release()

    answer = "".join(chunks)
    return {
        "id": completion_id,
        "object": "chat.completion",
        "created": created,
        "model": req.model,
        "choices": [
            {"index": 0, "message": {"role": "assistant", "content": answer}, "finish_reason": "stop"}
        ],
        "usage": _usage(req.messages, answer),
    }


async def _stream_body(gate: _Gate, completion_id: str, created: int, req: ChatCompletionRequest, msgs: list[dict], request: Request):
    def frame(delta: dict, finish: str | None = None) -> str:
        payload = {
            "id": completion_id,
            "object": "chat.completion.chunk",
            "created": created,
            "model": req.model,
            "choices": [{"index": 0, "delta": delta, "finish_reason": finish}],
        }
        return f"data: {json.dumps(payload, ensure_ascii=False)}\n\n"

    try:
        yield frame({"role": "assistant", "content": ""})
        async for piece in _upstream.astream(msgs, req.temperature, req.max_tokens):
            if await request.is_disconnected():
                return
            yield frame({"content": piece})
        yield frame({}, finish="stop")
        yield "data: [DONE]\n\n"
    except UpstreamError as exc:
        METRICS["upstream_errors"][exc.kind] = METRICS["upstream_errors"].get(exc.kind, 0) + 1
        yield f"data: {json.dumps({'error': {'message': exc.detail, 'type': f'upstream_{exc.kind}', 'code': exc.kind}})}\n\n"
        yield "data: [DONE]\n\n"
    finally:
        await gate.release()


DEMO_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "demo")
if os.path.isdir(DEMO_DIR):
    app.mount("/static", StaticFiles(directory=DEMO_DIR), name="static")


@app.get("/")
async def index():
    demo = os.path.join(DEMO_DIR, "index.html")
    if os.path.isfile(demo):
        return FileResponse(demo)
    return JSONResponse({"service": "ask-me-anything-2api", "docs": "/docs"})
