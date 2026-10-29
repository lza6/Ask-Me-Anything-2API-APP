# Ask Me Anything v1.7.0 — 逆向分析 + 2API 本地网关（第一轮）

从 `Ask Me Anything_v1.7.0 (PREMIUM).apk`（`com.chatgpt.aichat.gpt3.aichatbot`）还原出的
"Now AI" 白标 SaaS 聊天协议，以及一套可运行的本地 OpenAI 兼容网关 + demo + 压测。

**上游现状：真实 AI 服务端已全线不可用**（域名注销 / 服务超时 / 停放页 / key 失效），
详见 [`docs/UPSTREAM_STATUS.md`](docs/UPSTREAM_STATUS.md)。因此网关默认 `UPSTREAM=mock`，
把「前端 → 网关 → 适配器 → SSE」这条链路完整跑通；`UPSTREAM=nowai` 保留真实协议实现，
可在上游复活或指向自建同协议服务时一键切换。

协议细节：[`docs/PROTOCOL.md`](docs/PROTOCOL.md)

## 目录

```
gateway/app.py            OpenAI 兼容网关（FastAPI）：/v1/chat/completions /v1/models /healthz /metrics
gateway/upstreams.py      上游适配器：mock（零成本）| nowai（还原的 pass + HMAC-SHA512 协议）
demo/index.html           demo 页：模型选择 / 流式渲染 / 首包与 tok/s 实时统计
bench.py                  压测工具（并发、流式、注入速率、P50/P90/P99）
tools/sign.py             通行证签名工具（HMAC-SHA512 + base64 NO_WRAP）
tools/remoteconfig_dump.py  Firebase Remote Config 匿名拉取
tools/probe_upstream.py   上游存活探测矩阵（DNS/TCP/HTTP）
tools/real_probe.py       按还原协议直接打 nowai 入口，打印服务端原始响应（上游复活时一键复验）
tools/scenario_check.py   上游故障场景矩阵（8 个场景，起停真实子进程断言）
tools/e2e_demo.py         demo 页真实浏览器 E2E（Playwright）
sources-key-files/        关键反编译源码（协议出处，带 jadx 行号）
docs/NEW-API-INTEGRATION.md  **new-api 对接指南**（字段级配置 + 能力矩阵 + 错误码 + 切真实上游）
docs/PROTOCOL.md          协议还原（签名算法 / DTO / SSE / 模型表）
docs/UPSTREAM_STATUS.md   上游存活定案（逐主机证据）
docs/verification_log.md  第一轮全部实跑证据
Dockerfile / .dockerignore  容器化部署（对接联调用）
artifacts/                探测证据 / 压测报告 / 截图（含敏感配置，勿入库）
```

## 快速开始

```powershell
cd D:/apk-analysis/ama
pip install -r requirements.txt          # 仅运行网关；开发/验证用 requirements-dev.txt
python -m uvicorn gateway.app:app --host 127.0.0.1 --port 8960
# 浏览器打开 http://127.0.0.1:8960/
```

Docker：

```bash
docker build -t ama-2api:0.1.0 .
docker run -d --name ama-2api -p 8960:8960 -e UPSTREAM=mock ama-2api:0.1.0
```

**要接 new-api 请直接看 [`docs/NEW-API-INTEGRATION.md`](docs/NEW-API-INTEGRATION.md)**。

```bash
# OpenAI 兼容调用
curl -s http://127.0.0.1:8960/v1/chat/completions \
  -H 'Content-Type: application/json' \
  -d '{"model":"gpt-4o","messages":[{"role":"user","content":"hello"}],"stream":true}'
```

## 配置（环境变量）

| 变量 | 默认 | 说明 |
|---|---|---|
| `UPSTREAM` | `mock` | `mock` \| `nowai` |
| `MAX_CONCURRENCY` | `32` | 并发闸门（超出排队，排队超时返回 429 `gateway_busy`） |
| `QUEUE_TIMEOUT_S` | `20` | 排队最长等待 |
| `NOWAI_PASS_URL` | `http://pass-gpt.nowtechai.com/api/v1/pass` | 真实协议入口 |
| `NOWAI_HMAC_SECRET` | 无（自动读 `artifacts/remoteconfig.json`） | 签名密钥，公开仓库不含明文 |
| `NOWAI_TIMEOUT_S` | `60` | 上游超时 |
| `MOCK_FIRST_TOKEN_MS` / `MOCK_CHUNK_MS` | `0` / `0` | mock 首包与分片间隔 |
| `MOCK_FAIL_KIND` | `none` | `none`\|`timeout`\|`rate_limit`\|`provider_error`\|`malformed` |

## 已执行的验证（第一轮）

| 验证 | 命令 | 结果 |
|---|---|---|
| 端点冒烟 | `curl /healthz /v1/models /v1/chat/completions` | **已验证**：200，阻塞与流式均返回 OpenAI 结构 |
| 参数边界 | `model` 未知 / `max_tokens=999999` | **已验证**：422 拒绝（上界 `MAX_TOKENS_LIMIT=8192`） |
| 非流式压测 | `python bench.py -c 20 -n 300` | **已验证**：300/300 OK，QPS 359.9，P50 41.4ms / P99 187.9ms |
| 流式压测 | `python bench.py -c 20 -n 200 --stream` | **已验证**：200/200 OK，QPS 349.1，TTFT P50 28.9ms / P99 59.0ms |
| 高并发 | `python bench.py -c 50 -n 250` | **已验证**：250/250 OK，P50 108.8ms / P99 555.5ms（闸门排队生效） |
| 极限流式 | `python bench.py -c 100 -n 600 --stream` | **已验证**：600/600 OK，QPS 284.7，TTFT P50 207ms / P99 1108ms |
| 故障场景矩阵 | `python tools/scenario_check.py` | **已验证**：8/8 PASS（success/rate_limit/provider_error/malformed/refused/timeout/gate_busy/cancellation） |
| demo 浏览器 E2E | `python tools/e2e_demo.py` | **已验证**：填表→发送→流式渲染→统计面板→清空，0 console error，截图 `artifacts/demo_chat.png` |
| 上游存活 | `python tools/probe_upstream.py` | **已验证**：全线不可用（证据 `artifacts/upstream_probe.json`） |

未验证：真实 `nowai` 上游的响应格式（`content` 增量/全量、`status` 取值）——上游不可达，无样本。

## 与 new-api 对接

完整指南见 **[`docs/NEW-API-INTEGRATION.md`](docs/NEW-API-INTEGRATION.md)**（渠道字段级配置、模型倍率、
能力矩阵、错误码对照、切真实上游的步骤、mock 模式会真实扣费的警告）。

三条最容易踩的：

1. Base URL 填 `http://<网关>:8960`，**不带 `/v1`**；
2. 渠道类型 `OpenAI`（type=1），模型逐个填全 8 个，否则 422；
3. **mock 模式也要把模型倍率设 0**，否则 new-api 会照默认倍率真实扣费。

## 安全说明

`artifacts/remoteconfig.json` 含上游厂商的明文凭证（HMAC secret / 已失效的 OpenAI key /
GCP service account 私钥），`artifacts/` 与 `.env` 已在 `.gitignore` 中排除，**不要提交**。
本项目仅用于协议研究与本地链路验证，未发起任何付费上游调用。
