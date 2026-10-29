#!/usr/bin/env python3
"""Ask Me Anything v1.7.0 上游存活探测矩阵。

固化第一轮手工探测的全部关键点，输出可复现的证据 JSON。
只做 DNS / TCP / 单个 HTTP 请求，不产生任何付费调用。

用法:
    python tools/probe_upstream.py                # 走系统代理(HTTP_PROXY)
    python tools/probe_upstream.py --direct       # 强制直连
    python tools/probe_upstream.py -o artifacts/upstream_probe.json
"""
from __future__ import annotations

import argparse
import json
import socket
import time
import urllib.error
import urllib.request

# App v1.7.0 中硬编码 + Firebase Remote Config 中出现的全部上游主机
HOSTS = [
    "gpt.tolimanspaceni.com",       # BASE_URL —— NowAI 自建聊天网关
    "pass-gpt.nowtechai.com",       # /api/v1/pass —— HMAC 换取通行证
    "server.nowtechai.com",         # websocket 人工客服 + 备用 BASE_URL
    "art.nowtechai.com",            # 文生图 / 图生图
    "checking.heratech.space",      # BASE_URL_CHECKMESSAGE
    "data.tolimanspaceni.com",      # 静态资源(data/)
    "chatgpt.proxglobal.co",        # 新静态 CDN(v6.2.x 配置引用)，疑似现役 origin
]

DOH = "https://dns.google/resolve?name={name}&type=A"


def _opener(direct: bool) -> urllib.request.OpenerDirector:
    handlers = []
    if direct:
        handlers.append(urllib.request.ProxyHandler({}))
    return urllib.request.build_opener(*handlers)


def dns_lookup(name: str, op: urllib.request.OpenerDirector) -> dict:
    try:
        with op.open(DOH.format(name=name), timeout=10) as r:
            data = json.load(r)
        answers = sorted({a["data"] for a in data.get("Answer", []) if a.get("type") == 1})
        return {"status": data.get("Status"), "a": answers}
    except Exception as exc:  # noqa: BLE001 - 探测脚本需要吞掉一切网络异常
        return {"error": f"{type(exc).__name__}: {exc}"}


def tcp_probe(host: str, port: int, timeout: float = 6.0) -> str:
    sock = socket.socket()
    sock.settimeout(timeout)
    try:
        sock.connect((host, port))
        return "open"
    except Exception as exc:  # noqa: BLE001
        return type(exc).__name__
    finally:
        sock.close()


def http_probe(url: str, op: urllib.request.OpenerDirector, method: str = "GET") -> dict:
    req = urllib.request.Request(url, method=method, data=b"" if method == "POST" else None)
    req.add_header("User-Agent", "okhttp/4.12.0")
    if method == "POST":
        req.add_header("Content-Type", "application/json")
    started = time.time()
    try:
        with op.open(req, timeout=20) as r:
            body = r.read(400)
            return {"code": r.status, "ms": round((time.time() - started) * 1000), "head": body[:120].decode("utf8", "replace")}
    except urllib.error.HTTPError as exc:
        body = exc.read(240)
        return {"code": exc.code, "ms": round((time.time() - started) * 1000), "head": body[:200].decode("utf8", "replace")}
    except Exception as exc:  # noqa: BLE001
        return {"error": f"{type(exc).__name__}: {exc}", "ms": round((time.time() - started) * 1000)}


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--direct", action="store_true", help="绕过系统代理")
    ap.add_argument("-o", "--out", default="artifacts/upstream_probe.json")
    args = ap.parse_args()

    op = _opener(args.direct)
    report = {"generated_at": time.strftime("%Y-%m-%dT%H:%M:%S%z"), "mode": "direct" if args.direct else "proxy", "hosts": {}}

    for host in HOSTS:
        entry = {"dns": dns_lookup(host, op), "tcp": {}, "http": {}}
        for port in (80, 443):
            entry["tcp"][str(port)] = tcp_probe(host, port)
        entry["http"]["get_root"] = http_probe(f"http://{host}/", op)
        entry["http"]["post_api"] = http_probe(f"http://{host}/api/v1/pass", op, method="POST")
        report["hosts"][host] = entry
        print(f"{host:32s} dns={entry['dns']} tcp={entry['tcp']} get={entry['http']['get_root']} post={entry['http']['post_api']}")

    with open(args.out, "w", encoding="utf8") as fh:
        json.dump(report, fh, ensure_ascii=False, indent=2)
    print(f"\nwritten -> {args.out}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
