#!/usr/bin/env python3
"""Firebase Remote Config 拉取工具。

App 使用 firebase-remote-config SDK 的匿名 fetch，端点未做访问限制，
因此仅凭 APK 里的 public API key + appId 即可拉到完整配置（113 个键）。
本轮由此拿到了 key_hash_hmac_sha512 / open_ai_keys / google_service_account_key 等。

用法:
    python tools/remoteconfig_dump.py                 # 打印键清单
    python tools/remoteconfig_dump.py --dump          # 落盘全量 JSON（含敏感值，注意保管）
    python tools/remoteconfig_dump.py --key open_ai_keys
"""
from __future__ import annotations

import argparse
import json
import os
import sys
import urllib.request

PROJECT_ID = "chatgpt-android-f79c8"
API_KEY = "AIzaSyAUNtfTBD5VeUi_uzzb5ERS73FPSkR5EA8"  # res/values/strings.xml: google_api_key
APP_ID = "1:929029798693:android:e28c9b5479c0bd4a4738cc"
PACKAGE = "com.chatgpt.aichat.gpt3.aichatbot"
# Firebase Installation ID 可以是任意合法 UUID；RC 不对实例做校验
INSTANCE_ID = "a1b2c3d4e5f60718293a4b5c6d7e8f90"

ENDPOINT = (
    f"https://firebaseremoteconfig.googleapis.com/v1/projects/{PROJECT_ID}"
    f"/namespaces/firebase:fetch?key={API_KEY}"
)

SENSITIVE_KEYS = {"open_ai_keys", "google_service_account_key", "key_hash_hmac_sha512", "google_api_key"}


def fetch() -> dict:
    payload = {
        "app_instance_id": INSTANCE_ID,
        "app_id": APP_ID,
        "language_code": "en",
        "platform_version": "14",
        "time_zone": "UTC",
        "app_version": "1.7.0",
        "package_name": PACKAGE,
    }
    req = urllib.request.Request(
        ENDPOINT,
        data=json.dumps(payload).encode(),
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(req, timeout=30) as r:
        return json.load(r)


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--dump", action="store_true", help="把全量配置写入 artifacts/remoteconfig.json")
    ap.add_argument("--key", help="只输出单个键的值")
    ap.add_argument("--include-sensitive", action="store_true", help="打印时不解敏")
    args = ap.parse_args()

    data = fetch()
    entries = data.get("entries", {})

    if args.key:
        if args.key not in entries:
            print(f"key not found: {args.key}", file=sys.stderr)
            return 2
        print(entries[args.key])
        return 0

    if args.dump:
        os.makedirs("artifacts", exist_ok=True)
        path = "artifacts/remoteconfig.json"
        with open(path, "w", encoding="utf8") as fh:
            json.dump(data, fh, ensure_ascii=False, indent=2)
        print(f"written -> {path} ({len(entries)} keys)  !!! 含明文凭证，勿提交仓库 !!!")

    for k in sorted(entries):
        v = entries[k]
        flag = " [SENSITIVE]" if k in SENSITIVE_KEYS and not args.include_sensitive else ""
        print(f"{k:44s} {len(v):7d}B{flag}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
