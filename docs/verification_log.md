# 验证日志（第一轮实跑证据）

环境：Windows 10 / Python 3.14.3 / FastAPI 0.135.1 / uvicorn 0.41.0 / httpx 0.28.1 / Playwright(chromium)
网关：`python -m uvicorn gateway.app:app --host 127.0.0.1 --port 8960`，`UPSTREAM=mock`

## 1. 端点冒烟（已验证）

```
GET  /healthz            -> 200 {"ok":true,"upstream":"mock","in_flight":0}
GET  /v1/models          -> 200 8 个模型
POST /v1/chat/completions (阻塞) -> 200
  {"id":"chatcmpl-65ea7fed...","choices":[{"message":{"role":"assistant","content":"[mock-upstream] ..."},"finish_reason":"stop"}],
   "usage":{"prompt_tokens":3,"completion_tokens":60,"total_tokens":63}}
POST /v1/chat/completions (stream) -> 200 text/event-stream
  data: {"choices":[{"delta":{"role":"assistant","content":""},"finish_reason":null}]}
  data: {"choices":[{"delta":{"content":"[mock-up"},"finish_reason":null}]}
  ...
  data: {"choices":[{"delta":{},"finish_reason":"stop"}]}
  data: [DONE]
```

参数边界：

```
model=unknown        -> 422
max_tokens=999999    -> 422（上界 8192）
```

## 2. 压测（已验证，`bench.py`）

| 场景 | 命令 | OK | QPS | P50 | P90 | P99 | TTFT P50/P99 |
|---|---|---|---|---|---|---|---|
| 非流式 20c | `bench.py -c 20 -n 300` | 300/300 | 359.9 | 41.4ms | 107.6ms | 187.9ms | — |
| 流式 20c | `bench.py -c 20 -n 200 --stream` | 200/200 | 349.1 | 55.3ms | 69.8ms | 86.0ms | 28.9 / 59.0ms |
| 非流式 50c（闸门 32） | `bench.py -c 50 -n 250` | 250/250 | 303.1 | 108.8ms | 350.4ms | 555.5ms | — |
| 流式 100c | `bench.py -c 100 -n 600 --stream` | 600/600 | 284.7 | 277.3ms | 424.7ms | 1222.9ms | 207.2 / 1108.0ms |

原始报告：`artifacts/bench_blocking.json`、`artifacts/bench_stream.json`、`artifacts/bench_50c.json`

## 3. 故障场景矩阵（已验证，`tools/scenario_check.py`）

```
PASS  success             status=200
PASS  rate_limit          status=429 code=rate_limit
PASS  provider_error      status=502 code=provider_error
PASS  malformed_response  status=502 code=bad_response
PASS  network_refused     status=502 code=provider_error (ConnectError)
PASS  timeout             status=504 code=timeout (3.0s)
PASS  gate_busy           statuses=[429, 200, 429, 429] code=gateway_busy
PASS  cancellation        in_flight=0（客户端 6 次中途断开后信号量未泄漏）
8/8 passed
```

## 4. demo 浏览器 E2E（已验证，`tools/e2e_demo.py`）

```
upstream_badge = upstream: mock
answer[:80]    = [mock-upstream] 这是一条本地生成的回复，用于在真实上游不可达时验证网关链路与前端演示…
ttft=7 ms total=19 ms chars=93
console_errors = []
screenshot     = artifacts/demo_chat.png
E2E PASS
```

覆盖路径：页面加载 → `/healthz` 点亮上游徽标 → 选模型 → 改 max_tokens → 输入 → 发送 →
SSE 逐字渲染 → 首包/总耗时/字符/tok-s 面板更新 → 清空。

## 5. 官方 OpenAI SDK 兼容性（已验证）

用 `openai` 官方 Python SDK（`base_url=http://127.0.0.1:8960/v1`）实测：

```
chat.completions.create(..., )            -> OK，choices[0].message.content 正常，usage.total_tokens=62
chat.completions.create(..., stream=True) -> OK，11 个 content 分片，拼接文本 84 字符
models.list()                             -> ['gpt-4o-mini','gpt-4o','o1','claude', ...]
```

## 6. 签名正确性（已验证）

```
RC secret len = 267, code secret len = 267, identical = True
固定 ts=1793310000123，两处独立实现：
  tools/sign.py 产出 vs 直接用 RC 原值算 HMAC-SHA512 -> key match = True
```

## 7. 上游存活（已验证，`tools/probe_upstream.py`）

见 `docs/UPSTREAM_STATUS.md` 与 `artifacts/upstream_probe.json`。结论：主聊天链路全线不可用。

OpenAI key 验证（只用免费端点）：

```
POST https://api.openai.com/v1/moderations  (RC open_ai_keys[0])
-> 401 {"code":"invalid_api_key","message":"Incorrect API key provided: sk-proj-***...hlkA"}
```

## 8. 未执行 / 阻塞

| 项 | 原因 |
|---|---|
| `UPSTREAM=nowai` 打真实上游的成功路径 | 上游不可达（域名注销 / TCP 超时 / CF 401） |
| 真实 SSE `content` 增量/全量语义 | 无真实响应样本 |
| new-api 渠道联通 | 需上游可用后才谈对接；本轮未接入 |
