# new-api 对接指南

> 面向对象：在新会话里执行对接的人（不需要读逆向细节）。
> 网关代码在本仓库 `gateway/`，协议背景见 [`PROTOCOL.md`](PROTOCOL.md)，上游状态见 [`UPSTREAM_STATUS.md`](UPSTREAM_STATUS.md)。

---

## 0. 先读这一节，否则一定白干

**本网关当前只有 `mock` 能力。** App 的真实 AI 上游已确认全线不可用
（主域 NXDOMAIN / pass 服务超时 / 内置 OpenAI key 401 / 新 CDN 域 POST 被 Cloudflare 全拦）。

所以下面这套对接有三种用途，请在动手前确认自己要做哪一种：

| 目的 | 可行性 | 说明 |
|---|---|---|
| A. 验证 new-api 渠道配置、流式中继、计费形状是否正确 | ✅ 完全可做 | 用 `UPSTREAM=mock`，模型名称/响应结构/SSE 都是真的，只有文本是本地生成的 |
| B. 做成能给终端用户用的真渠道 | ❌ 现在不行 | 必须先有新上游（见第 8 节） |
| C. 把本网关当 new-api 的「协议适配层」，等上游复活 | ✅ 可以 | `UPSTREAM=nowai` 已实现完整签名+SSE 解析，改一个环境变量就切 |

**mock 模式下 new-api 仍会按 token 正常扣费**（它按响应里的 usage 计费），
所以对接 mock 渠道时务必把模型倍率设成 0 或放进独立分组，否则会把真实用户余额扣光。

---

## 1. 启动网关

### 1.1 本机直接跑

```bash
cd D:/apk-analysis/ama          # 或 clone 下来的仓库目录
pip install -r requirements.txt
python -m uvicorn gateway.app:app --host 127.0.0.1 --port 8960
```

自检（三条都必须通）：

```bash
curl -s http://127.0.0.1:8960/healthz
# {"ok":true,"upstream":"mock","in_flight":0}

curl -s http://127.0.0.1:8960/v1/models
# {"object":"list","data":[{"id":"gpt-4o-mini",...}]}

curl -s http://127.0.0.1:8960/v1/chat/completions \
  -H 'Content-Type: application/json' \
  -d '{"model":"gpt-4o","messages":[{"role":"user","content":"hi"}],"max_tokens":64}'
# {"id":"chatcmpl-...","choices":[{"message":{"role":"assistant","content":"[mock-upstream] ..."}}],...}
```

> Windows + Git Bash 下用 `curl -d '{"...中文..."}'` 可能因编码被拒（返回 `There was an error parsing the body`）。
> 把 JSON 存成 UTF-8 文件再用 `--data-binary @file.json` 即可。

### 1.2 Docker（推荐给正式一点的联调）

仓库根目录已带 `Dockerfile`：

```bash
docker build -t ama-2api:0.1.0 .
docker run -d --name ama-2api -p 8960:8960 -e UPSTREAM=mock ama-2api:0.1.0
```

### 1.3 关键环境变量

| 变量 | 默认 | 说明 |
|---|---|---|
| `UPSTREAM` | `mock` | `mock` / `nowai` |
| `MAX_CONCURRENCY` | `32` | 单进程并发上限；超出排队，排队超时返回 429 |
| `QUEUE_TIMEOUT_S` | `20` | 排队等待上限 |
| `NOWAI_PASS_URL` | `http://pass-gpt.nowtechai.com/api/v1/pass` | 仅 `UPSTREAM=nowai` 用 |
| `NOWAI_HMAC_SECRET` | 无（自动读 `artifacts/remoteconfig.json`） | 仅 `nowai` 用；公开仓库不含明文 |
| `NOWAI_TIMEOUT_S` | `60` | 上游超时 |

---

## 2. new-api 渠道配置（字段级）

在 new-api 后台「渠道 → 添加渠道」：

| 字段 | 填什么 | 备注 |
|---|---|---|
| 类型 | **OpenAI**（`type = 1`） | 别选「OpenAI 兼容」以外的自建类型 |
| 名称 | `AskMeAnything-mock`（或 `AMA-nowai`） | 建议带上游标识，方便排查 |
| 分组 | 单独建一个分组，如 `ama-test` | 避免 mock 流量污染正式分组 |
| 模型 | 见下方 8 个模型 | 必须逐个填入，new-api 据此做模型路由 |
| 密钥 | 任意非空字符串，如 `sk-local-ama` | 本网关不校验鉴权；填非空是为了绕过 new-api 的必填校验 |
| 代理 | 留空 | 若 new-api 与网关不在同一台机，填 `http://<网关内网IP>:8960` 不行——那是 Base URL 的活；此处只在需要出网代理时填 |
| 自定义 Base URL | `http://<网关IP>:8960` | **不要带 `/v1`**，new-api 会自己拼 `/v1/chat/completions` |
| 权重 | 1 | — |
| 优先级 | 0 | — |
| 自动禁用 | 关（或按需） | mock 渠道不需要 |

模型列表（与服务端 `/v1/models` 完全一致，写错会 422）：

```
gpt-4o-mini
gpt-4o
o1
claude
gemini-3.0-pro
deepseek-r1
grok
llama-4
```

### 2.1 计费倍率（mock 模式必做）

「设置 → 模型倍率」里，把这 8 个模型**全部加进去**并把倍率设为 `0`（或极小的值）：

- 不加：new-api 会用默认倍率（`gpt-4o` 一类通常 2.5 以上），mock 也会真实扣费
- 加了但设 `0`：调用不扣额度，适合纯链路验证
- 加了按真实价格设：只有在 `UPSTREAM=nowai` 且上游真的能出字时才有意义

同理「补全倍率」也建议设 0，否则 prompt 侧还会再算一次。

### 2.2 渠道「测试」按钮

new-api 的测试按钮打的是 `/v1/models`（不是 chat）。本网关**已实现该端点**，所以测试会直接通过：

```
渠道测试 → 成功（列表返回 8 个模型）
```

如果测试失败，先手工 `curl http://<网关>:8960/v1/models` 确认是网关问题还是网络问题。

---

## 3. 端到端验证

### 3.1 直连网关（跳过 new-api）

```bash
curl -s -N http://127.0.0.1:8960/v1/chat/completions \
  -H 'Content-Type: application/json' \
  -d '{"model":"gpt-4o","messages":[{"role":"user","content":"ping"}],"stream":true}' | head -5
```

### 3.2 经 new-api 中转

```bash
curl -s -N http://<new-api>/v1/chat/completions \
  -H "Authorization: Bearer <你的 new-api token>" \
  -H 'Content-Type: application/json' \
  -d '{"model":"gpt-4o","messages":[{"role":"user","content":"ping"}],"stream":true}' | head -5
```

判定通过的标准：

1. 返回的 `data:` 分片里 `object` 是 `chat.completion.chunk`；
2. 最后一个分片 `finish_reason` 为 `stop`，随后有 `data: [DONE]`；
3. new-api 日志里该次调用记录的状态是成功、token 数与响应 usage 一致。

### 3.3 用官方 SDK 交叉验证（可选）

```python
from openai import OpenAI
c = OpenAI(base_url="http://<网关>:8960/v1", api_key="sk-local-ama")
print(c.chat.completions.create(model="gpt-4o",
      messages=[{"role": "user", "content": "ping"}]).choices[0].message.content)
```

---

## 4. 能力矩阵（渠道会被打哪些接口）

| new-api 功能 | 本网关 | 说明 |
|---|---|---|
| `/v1/chat/completions` 非流式 | ✅ | OpenAI 结构完整，含 usage |
| `/v1/chat/completions` 流式 | ✅ | `data:` 分片 + `[DONE]`；带 `X-Accel-Buffering: no` |
| `/v1/models` | ✅ | 渠道测试用 |
| `/v1/completions`（旧补全） | ❌ 404 | 未实现 |
| `/v1/embeddings` | ❌ 404 | 未实现 |
| `/v1/images/generations` | ❌ 404 | App 的文生图走另一个已死的域，未移植 |
| `/v1/audio/*` | ❌ 404 | — |
| function calling / tools | ❌ 不转发 | 请求里带 `tools` 会被忽略（上游协议没有该字段） |
| 多模态（image_url 内容块） | ❌ | `content` 只接受字符串，传数组会 422 |

渠道侧建议：**只把这 8 个 chat 模型挂到该渠道**，不要把 embeddings/image 模型也挂上去，否则会持续 404 并把渠道健康度打红。

---

## 5. 错误码对照（排查用）

| 现象 | 网关返回 | 原因 / 处理 |
|---|---|---|
| 422 | `{"detail":[...]}` | 模型名不在列表 / `max_tokens > 8192` / `messages` 为空或超过 100 条 / 单条 content 超 32000 字符 / `temperature` 超出 0–2 |
| 429 `gateway_busy` | — | 并发闸门满且排队超时。调大 `MAX_CONCURRENCY` 或 `QUEUE_TIMEOUT_S` |
| 429 `rate_limit` | — | 上游限流（`UPSTREAM=nowai` 时才有） |
| 502 `provider_error` | — | 上游 4xx/5xx 或连接被拒（`nowai` 模式下上游已死时就是这个） |
| 502 `bad_response` | — | 上游返回了非 SSE 的垃圾数据 |
| 504 `timeout` | — | 上游超时，调 `NOWAI_TIMEOUT_S` |
| SSE 里出现 `{"error":...}` 分片 | — | 流已开始后上游出错，网关无法再改 HTTP 状态，只能带内报错 |

排查顺序：`/healthz` → 直连 `/v1/chat/completions` → 再看 new-api 日志 → 最后才怀疑 new-api 配置。

---

## 6. 切到真实协议（上游复活 / 指向自建服务时）

```bash
export UPSTREAM=nowai
export NOWAI_PASS_URL=http://<真实入口>/api/v1/pass
export NOWAI_HMAC_SECRET=<从 Remote Config 的 key_hash_hmac_sha512 取，或先跑 tools/remoteconfig_dump.py --dump>
python tools/real_probe.py --stream        # 先用探针确认协议是否还通
python -m uvicorn gateway.app:app --host 0.0.0.0 --port 8960
```

`tools/real_probe.py` 会打印签名、请求体和服务端原始响应，**协议如果变了会立刻看出来**，
比直接接 new-api 再猜要快得多。

切换前务必先跑 `python tools/scenario_check.py`，确保 8 个故障场景仍然全绿（它是回归基线）。

---

## 7. 安全与运维注意

- 网关**不做鉴权**（`api_key` 随便填），只应监听内网/本机。要暴露公网请在前面加反向代理 + 鉴权。
- `artifacts/` 含上游明文凭证（HMAC secret、已失效的 OpenAI key、GCP service account 私钥），
  已 gitignore，**不要打进镜像、不要提交、不要贴进群里**。
- 压测不要和 new-api 生产实例放同一台机器（`bench.py` 会打满 CPU）。
- 网关是单进程 asyncio，吞吐上限约 300–360 QPS（本机实测），需要更高就多开端口 + 反代轮询。

---

## 8. 如果要真正打通上游（现阶段的唯一路径）

1. **拿到 v6.2.x 新版 APK**（本机只有 1.7.0，内部版本串已指向 `Version 6.2.1.9`）。
   新版的 base URL 常量才可能指向活着的 API 域；`chatgpt.proxglobal.co` 目前只有静态资源可用。
2. 新版配置里已出现 `config_integrity_token_ttl_sec`（Play Integrity 令牌时长），
   说明 v6.x 引入了客户端完整性校验，届时大概率需要真机/AVD + Frida 伪造。
3. 拿到新域名后：`python tools/probe_upstream.py` → `python tools/real_probe.py --stream` →
   两关都过，本网关只需改 `NOWAI_PASS_URL` 即可继续用，`gateway/` 代码不用动。
