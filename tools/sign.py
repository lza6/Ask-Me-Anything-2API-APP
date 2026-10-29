#!/usr/bin/env python3
"""Ask Me Anything v1.7.0 通行证签名工具（等价实现）。

还原自 com.android.ntduc.chatgpt.data.DataRepository#startStreamNowAiServer（smali 反编译）：

    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
    long ts = cal.getTimeInMillis();
    String secret = RemoteConfig("key_hash_hmac_sha512");   // 取不到则回落到内联常量
    Mac mac = Mac.getInstance("HmacSHA512");
    mac.init(new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA512"));
    String key = Base64.encodeToString(mac.doFinal(String.valueOf(ts).getBytes(UTF_8)), Base64.NO_WRAP);

请求：
    POST {BASE_PASS}/api/v1/pass
    Key: <key>          # base64(HMAC-SHA512(secret, ms_timestamp))，NO_WRAP
    TimeStamps: <ts>    # 与参与签名的毫秒时间戳一致
    Content-Type: application/json
    body: {"contents":[{"role":"user","content":"hi"}]}

密钥来源（按优先级）：
    1. 环境变量 NOWAI_HMAC_SECRET
    2. 本地 artifacts/remoteconfig.json 里的 key_hash_hmac_sha512（tools/remoteconfig_dump.py 可拉取）
    3. 自行从 APK 的 Remote Config 常量中提取

公开仓库刻意不放明文密钥。
"""
from __future__ import annotations

import base64
import hashlib
import hmac
import json
import os
import time

_HERE = os.path.dirname(os.path.abspath(__file__))
_RC_CACHE = os.path.join(os.path.dirname(_HERE), "artifacts", "remoteconfig.json")


class SecretNotFound(RuntimeError):
    pass


def load_secret() -> str:
    """解析 HMAC 密钥：环境变量 > 本地 Remote Config 转储。」"""
    env = os.getenv("NOWAI_HMAC_SECRET", "").strip()
    if env:
        return env
    if os.path.isfile(_RC_CACHE):
        try:
            with open(_RC_CACHE, encoding="utf8") as fh:
                return json.load(fh)["entries"]["key_hash_hmac_sha512"]
        except (KeyError, json.JSONDecodeError) as exc:
            raise SecretNotFound(f"remoteconfig 转储里没有 key_hash_hmac_sha512: {exc}") from exc
    raise SecretNotFound(
        "缺少 HMAC 密钥。请设置 NOWAI_HMAC_SECRET，或先运行 "
        "`python tools/remoteconfig_dump.py --dump` 生成 artifacts/remoteconfig.json。"
    )


def now_ms() -> int:
    """App 使用 Asia/Bangkok 时区的 Calendar.getTimeInMillis()，其结果与该时区无关（epoch 毫秒恒等）。"""
    return int(time.time() * 1000)


def sign(timestamp_ms: int | None = None, secret: str | None = None) -> tuple[str, int]:
    """返回 (Key, TimeStamps)。"""
    ts = now_ms() if timestamp_ms is None else int(timestamp_ms)
    key_secret = secret if secret else load_secret()
    digest = hmac.new(key_secret.encode("utf-8"), str(ts).encode("utf-8"), hashlib.sha512).digest()
    return base64.b64encode(digest).decode("ascii"), ts


if __name__ == "__main__":
    k, t = sign()
    print(f"TimeStamps: {t}")
    print(f"Key: {k}")
