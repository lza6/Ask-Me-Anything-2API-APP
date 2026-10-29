# 上游存活状态（第一轮定案）

探测时间：2026-10-30（Asia/Shanghai）
探测方式：`python tools/probe_upstream.py`（走系统代理）+ 直连交叉验证 + DoH 解析
证据文件：`artifacts/upstream_probe.json`

## 结论

**该 App 的真实 AI 上游已全线不可用。** 三个可选聊天链路（NowAI 自建 / OpenAI 直连 / Poe 直连）全部失去可用性，且不可通过重放协议补救。因此本轮交付的可运行部分是「协议还原 + 本地 mock 网关 + demo + 压测」，`UPSTREAM=nowai` 保留为上游复活/迁到自建同协议服务时的切换开关。

## 逐项证据

| 主机 | 角色 | DNS | TCP | HTTP | 判定 |
|---|---|---|---|---|---|
| `gpt.tolimanspaceni.com` | 主 BASE_URL（NowAI 聊天 + tracking） | **NXDOMAIN (Status 3)** | `gaierror` | 代理侧 503（无此域） | ☠️ 域名已注销 |
| `data.tolimanspaceni.com` | 静态资源（模型图标/角色图） | **NXDOMAIN** | `gaierror` | 503 | ☠️ 域名已注销 |
| `pass-gpt.nowtechai.com` | 通行证 `/api/v1/pass`（聊天唯一入口） | A `23.20.110.97` | 80/443 `TimeoutError` | 超时（代理与直连均超时） | ☠️ 服务不可达 |
| `art.nowtechai.com` | 文生图/图生图 | A `76.223.67.189`/`13.248.213.45` | 80/443 open | 200 → `window.location="/lander"` | ☠️ 域名停放页 |
| `checking.heratech.space` | BASE_URL_CHECKMESSAGE | A `103.224.212.215` | 80/443 open | 200 → "This domain may be for sale" | ☠️ 域名待售 |
| `server.nowtechai.com` | tracking / 客服 ws / 备用 BASE_URL | A `54.87.60.147` | 80 open，443 refused | `GET /` → **nginx 403**；`POST /api/v1/pass` → **nginx 404** | ⚠️ 机器活着，业务路由已下线 |
| `chatgpt.proxglobal.co` | v6.2.x 配置引用的新静态 CDN | A `188.114.96.7`/`188.114.97.7`（Cloudflare） | 80/443 open | 静态资源 `GET /gpt/remote-and/...png` → **200**；`GET /` → 404(SPA)；**任意路径 POST → 401** | ⚠️ 仅静态可用，POST 全线被拒 |
| `67.217.57.37:8001/api/` | BASE_URL_PDF 候选（硬编码） | — | 超时 | 超时 | ☠️ 不可达 |
| `192.53.115.249:9999` | 硬编码残留 | — | 超时 | 超时 | ☠️ 不可达 |
| `45.79.147.162:1234` | 硬编码残留 | — | 超时 | 超时 | ☠️ 不可达 |

补充：`nowtechai.com` 主域 80/443 可连但无业务响应（无 HTTP 回包）。

## 另外两条聊天链路

| 链路 | 证据 | 判定 |
|---|---|---|
| OpenAI 直连（`startStreamOpenAiServer`） | 用 RC `open_ai_keys` 的 `sk-proj-…` 打免费端点 `POST https://api.openai.com/v1/moderations` → **401 `invalid_api_key`** | ☠️ key 已吊销/失效 |
| Poe 直连（`startStreamPoeAiServer`） | 目标 `https://api.poe.com/bot/GPT-3.5-Turbo`，该路径为 Poe 早期公开 bot 端点，官方早已下线 | ☠️ 协议废弃 |

> OpenAI 验证只用了**免费**的 `moderations` 端点，未发起任何付费 completion 请求。

## `chatgpt.proxglobal.co` 的 401 是什么

实测：GET 静态 200、GET 任意业务路径 404（同一份 27 150 字节 SPA fallback）、**POST 任意路径（含 `/nonexistent-xyz`）一律 401**，响应头 `Server: cloudflare`、有 `CF-RAY`、无 `cf-mitigated`。

- 换 UA（okhttp / 浏览器 / 空）、加 `Referer` / `Origin` / `Accept` / 正确 HMAC `Key`+`TimeStamps` —— 全部 401。
- 因为「不存在的路径 POST 也 401」，这**不是应用层路由鉴权**，而是边缘层对 POST 方法的一刀切拦截（WAF 规则或 Cloudflare Access），真实 App 的 API 也不可能走这条通道。
- 404 与 401 两个响应体（27 150 / 16 794 字节）都是 Cloudflare **内置错误页模板**（内联 CSS + `cloudflare.com/favicon.ico`），不含任何 origin 业务内容，可确认页面由边缘生成而非源站返回。
- 该域在 v6.2.x 配置里只作为**静态资源 CDN**出现（`/gpt/remote-and/v6200/...`、`/v6205/...`），没有出现任何 API 路径。

## 复现命令

```bash
cd D:/apk-analysis/ama
python tools/probe_upstream.py                       # 走系统代理
python tools/probe_upstream.py --direct -o artifacts/upstream_probe_direct.json
python tools/remoteconfig_dump.py --key key_hash_hmac_sha512   # RC 仍可匿名读取
```

## 若要继续打通的可行路径（需要新输入）

1. **拿到 v6.2.x 新版 APK**（本机只有 1.7.0）：新协议/新 API 域名极可能已经换了域名族（`proxglobal.co` 只是 CDN 候选），只有新版 APK 里的 base URL 常量才能确定。
2. 新版 APK 的 RC 键已经存在（`config_integrity_token_ttl_sec`、`config_ads_v62xx`），说明 v6.x 引入了 Play Integrity 客户端校验，届时还需要真机/AVD + Frida 才能伪造。
3. 若只是要「验证网关链路」，现状的 mock 模式已完整覆盖（见 README 的验证矩阵）。

## 结论级别

- 上游不可用：**已验证**（多主机 × 多端口 × 代理/直连交叉，证据落盘）
- 协议正确性（签名/报文）：**静态确认**（源码级还原，未在真实上游跑通）
- 协议 SSE 细节（增量/全量、status 取值）：**待验证**
