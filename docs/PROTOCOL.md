# Ask Me Anything v1.7.0 — 协议还原

> 目标 APK：`Ask Me Anything_v1.7.0 (PREMIUM).apk`
> 包名：`com.chatgpt.aichat.gpt3.aichatbot`（业务代码包 `com.android.ntduc.chatgpt`）
> 内部版本串：`Now_AI_V4.3.0.0_26.07.2024_12h45_release`（编译时间 2024-08-03）
> 技术栈：Kotlin + Hilt(Dagger) + Ktor(流式) + Retrofit(辅助接口) + kotlinx.serialization
> 加固：无 DexProtector/pairip；仅 ads SDK（AppLovin/AdColony/Unity/Pangle/Fyber…）+ 混淆

## 1. 应用身份

该 App 是 **"Now AI" 白标 SaaS** 的一个皮肤（同一套后端被 proxglobal / nowtechai 名下多个 App 复用）：

| 证据 | 位置 |
|---|---|
| 版本串 `Now_AI_V4.3.0.0_...` 出现在每个类的 `@Metadata` 里 | 全包 |
| 购买/计费/远程配置模块包名 `com.proxglobal.*` | `com/proxglobal/proxpurchase`, `com/proxglobal/remoteconfig` |
| 静态资源目录 `/gpt/remote-and/v<版本>/...` | 远端配置 |
| 8 个模型、19 个 AI-Art 风格、9 个角色卡 | 远端配置 |

**签名状态**：证书主体 `C=IN`、`SHA1withRSA`、**1024 位 RSA**、2020-11-01 签发 / 2045 到期。
Google Play 分发应用均为 2048 位，故本包是**第三方重签的 Premium mod 版**（非官方签名）。
`PREMIUM` 解锁完全发生在客户端（配额与 `premium` 标记都在 SharedPreferences 与远端配置里，
无服务端校验），因此换签名不影响任何协议行为——本轮分析结论对官方包同样成立。

## 2. 三条聊天链路

`ChatViewModel` 可按 `BOT_SELECT` 在三种上游间切换，协议彼此独立：

| 链路 | 入口 | 鉴权 | 现状 |
|---|---|---|---|
| `startStreamNowAiServer` | `POST {pass-gpt}/api/v1/pass` | HMAC-SHA512 时间戳签名 | **上游不可用**，见 UPSTREAM_STATUS.md |
| `startStreamOpenAiServer` | `https://api.openai.com/v1/chat/completions` | RC 下发的 `open_ai_keys` | key 已失效（401 invalid_api_key） |
| `startStreamPoeAiServer` | `https://api.poe.com/bot/GPT-3.5-Turbo` | RC `poe_key` | 该端点早已废弃 |

三者返回统一的 `NowAIResponse` 流，UI 层无差异。

## 3. NowAI 协议（主链路）

### 3.1 请求

```
POST http://pass-gpt.nowtechai.com/api/v1/pass
Key: <base64(HMAC-SHA512(secret, str(TimeStamps)))>
TimeStamps: <epoch 毫秒>
Content-Type: application/json

{"contents":[{"role":"system","content":"..."},{"role":"user","content":"..."}]}
```

### 3.2 签名算法（完整还原）

源码：`DataRepository#mo2667k`（jadx 命名）→ `sources-key-files/data/DataRepository.java`

```java
Calendar calendar = Calendar.getInstance();
calendar.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
long timeInMillis = calendar.getTimeInMillis();

String secretKey = remoteConfig.getString("key_hash_hmac_sha512");  // 失败则回落到内联常量
Mac mac = Mac.getInstance("HmacSHA512");
mac.init(new SecretKeySpec(secretKey.getBytes(UTF_8), "HmacSHA512"));
String key = Base64.encodeToString(mac.doFinal(String.valueOf(timeInMillis).getBytes(UTF_8)), 2); // 2 = NO_WRAP
```

要点：

- 参与签名的是**毫秒时间戳的十进制字符串**，不是报文，不是 body。
- `Key` 是无换行 base64（Java `Base64.NO_WRAP`）。
- `TimeStamps` 头与参与签名的值是同一个 long，服务端据此防重放并校验 HMAC。
- 时间源为设备时钟（`Calendar.getInstance()`），无服务端校时 → 本机时钟偏移会直接 401。
- `Asia/Bangkok` 时区对 `getTimeInMillis()` 无影响（epoch 毫秒与时区无关），仅影响 `Calendar` 字段。
- secret 在 **Firebase Remote Config**（键 `key_hash_hmac_sha512`）内联了一份相同值作为 fallback，两份一致，因此纯静态即可取到（`tools/sign.py`）。

等价实现：`tools/sign.py`（已核对两份 secret 完全一致）。

### 3.3 报文 DTO

Kotlin `@Serializable` 定义（`data/dto/now_ai/`）：

```kotlin
@Serializable data class NowAiRequest(val contents: List<NowAIContent>)
@Serializable data class NowAIContent(val role: String, val content: String)   // role: system|user|assistant
@Serializable data class NowAIResponse(val content: String, val status: String)
@Serializable data class NowAiError(val detail: String)
```

> **注意**：v1.7.0 的请求体**不含 model 字段**，模型由服务端按账号/IAP 决定。
> `config_chat_model` 里那 8 个模型（type 102–109）属于 v6.2.x 的新版能力，未体现在 1.7.0 的协议中。

### 3.4 响应

`text/event-stream`，逐行解析（Ktor `ByteReadChannel` → `readUTF8Line` → `json.decodeFromString<NowAIResponse>`）：

```
{"content":"增量或累积文本","status":""}
{"content":"...","status":"done"}
```

`status` 语义与 `content` 是增量还是全量，**待验证**（上游不可达，无法抓真实响应）；网关按宽松策略解析：兼容裸 JSON 行与 `data:` 前缀行，`status ∈ {done,stop,finish,end}` 视为结束。

### 3.5 会话组装（`ChatViewModel#handleStreamNowAiServerChatEvent`）

```kotlin
contents = [ {"role":"system","content": systemPrompt} ] + history
// TypeChat.SHORT  -> 末条用户消息追加 ". (Short Answer)"
// TypeChat.MEDIUM -> 末条用户消息追加 ". (Maximum is 100 words)"
```

即「回答长度档位」是**客户端在用户消息尾部拼字符串**实现的，服务端不认识独立参数。

## 4. 辅助接口（Retrofit，非聊天）

| Service | Base URL 常量 | 路由 |
|---|---|---|
| `TrackingChatService` | `http://server.nowtechai.com/` | `GET chat/send?time&version`、`POST gpt-service/gather` |
| `SupportPurchasedService` | `http://server.nowtechai.com/` | `GET gpt-service/message/{token}` |
| `TrackingTokenService` | `http://checking.heratech.space/` | `GET checking/gpt/pass?pass&gpt`、`GET checking/gpt/token?key&value` |
| `TrackingSeverErrorService` | `BASE_URL_CHECKSERVERERROR` | `GET gpt-error?content`、`POST gpt-report` |
| `ChatPDFService` | `BASE_URL_PDF` | `POST {version}/chatpdf`、`POST {version}/file/upload` |
| `ImageGenNowTechService` | `http://art.nowtechai.com/` | `GET art?name=` |
| `FramesService` | `BASE_URL_PDF` | `GET frames.json` |
| `OpenAiService` | `https://api.openai.com/v1/` | `POST moderations`（用 RC 的 key 做内容审核） |

Dagger 中硬编码的 base（`DaggerApp_HiltComponents_SingletonC`）：

```
http://67.217.57.37:8001/api/     (BASE_URL_PDF 候选)
http://art.nowtechai.com/
http://server.nowtechai.com/      ×2（tracking + support）
http://gpt.tolimanspaceni.com/    ← 主 BASE_URL
https://api.openai.com/v1/
```

另有两个内置 IP 常量：`192.53.115.249:9999`、`45.79.147.162:1234`（无任何路由引用，疑为开发残留，实测不通）。

## 5. 模型与配额（来自远端配置，非 APK 内）

`config_chat_model`（8 项，`premium` 标记决定是否要求内购）：

| type | 名称 | premium |
|---|---|---|
| 102 | GPT-4o mini | 否 |
| 103 | GPT-4o | 是 |
| 104 | GPT-o1 | 是 |
| 105 | Claude | 是 |
| 106 | Gemini 3.0 Pro | 是 |
| 107 | DeepSeek-R1 | 是 |
| 108 | Grok | 是 |
| 109 | Llama-4 | 是 |

配额策略（`credit_free_user` / `credit_free_style_user`）：

```
{"freePerInstall":10,"freePerDay":10}     # 普通聊天：安装 10 次 + 每日 10 次
文生图/图生图/logo/签名/联网搜索/文档：creditPerDay=0（即当前全部关闭）
```

配额判定**纯客户端**（SharedPreferences 计数），服务端不校验；`prevent_install` 里有 100+ 个「禁止安装」的包名（各种文件管理器/浏览器/MT管理器/模拟器），装在这些环境会直接拦截启动。

## 6. Firebase Remote Config 面（可匿名读取）

- project `chatgpt-android-f79c8`，API key 在 `res/values/strings.xml`（`google_api_key`）
- 端点：`POST https://firebaseremoteconfig.googleapis.com/v1/projects/{project}/namespaces/firebase:fetch?key={apiKey}`
- 无访问限制：任意 `app_instance_id` 即可拉全量 **113 个键**（`tools/remoteconfig_dump.py` 可复现）
- 泄露内容（本地 `artifacts/remoteconfig.json`，**不入库**）：
  - `key_hash_hmac_sha512` —— 通行证签名密钥
  - `open_ai_keys` —— 1 个 `sk-proj-…`（实测已 401 失效）
  - `google_service_account_key` —— 一个 GCP service account 私钥（`data-segment-459807`）
  - `google_api_key`、`config_integrity_token_ttl_sec`（v6.x 才启用的 Play Integrity 时长）、广告位全量配置

> 这是**上游厂商的安全缺陷**，与本地网关无关；报告里只记录，不做进一步利用。

## 7. 待验证项

| 项 | 原因 |
|---|---|
| SSE `content` 是增量还是全量 | 上游不可达，无真实响应样本 |
| `status` 的全部取值与含义 | 同上 |
| 模型（102–109）在 1.7.0 是否真的可选 | 请求体无 model 字段，v6.2.x 协议未获得 |
| `BASE_URL_CHECKSERVERERROR` / `BASE_URL_PDF` 的确切域名 | 常量被 R8 内联，Dagger 侧未直接绑定 |
| 服务端是否校验 UA / 客户端证书 | 无可用入口 |
