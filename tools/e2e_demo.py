#!/usr/bin/env python3
"""demo 页真实浏览器端到端验证（Playwright chromium，headless）。

覆盖：页面加载 -> 选择模型 -> 输入 -> 点击发送 -> SSE 流式渲染 -> 统计面板更新。
产出截图到 artifacts/。

用法:
    python tools/e2e_demo.py
"""
from __future__ import annotations

import os
import sys

from playwright.sync_api import sync_playwright

BASE = os.getenv("GATEWAY_BASE", "http://127.0.0.1:8960")
OUT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "artifacts")


def main() -> int:
    os.makedirs(OUT, exist_ok=True)
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page(viewport={"width": 1280, "height": 900})
        errors: list[str] = []
        page.on("pageerror", lambda e: errors.append(str(e)))

        page.goto(BASE + "/", wait_until="networkidle")
        assert page.title().startswith("Ask Me Anything"), page.title()

        # 上游徽标应被 /healthz 点亮
        page.wait_for_function("document.getElementById('dot').classList.contains('on')", timeout=8000)
        upstream_badge = page.inner_text("#upstream-badge")
        page.select_option("#model", "deepseek-r1")
        page.fill("#maxtok", "256")

        page.fill("#input", "用一句话说明这个 demo 在验证什么")
        page.click("#send")

        # 等待助手消息体出现内容且统计面板写入总耗时
        page.wait_for_function(
            "(() => {const els=document.querySelectorAll('.msg.assistant .body');"
            "return els.length>0 && els[els.length-1].textContent.trim().length>20;})()",
            timeout=20000,
        )
        page.wait_for_function("document.getElementById('total').textContent.includes('ms')", timeout=20000)

        answer = page.eval_on_selector_all(".msg.assistant .body", "els => els[els.length-1].textContent")
        ttft = page.inner_text("#ttft")
        total = page.inner_text("#total")
        chars = page.inner_text("#chars")
        tps = page.inner_text("#tps")

        page.screenshot(path=os.path.join(OUT, "demo_chat.png"), full_page=True)

        # 清空按钮路径
        page.click("#clear")
        page.wait_for_function("document.getElementById('log').childElementCount === 0", timeout=5000)

        print(f"upstream_badge = {upstream_badge}")
        print(f"answer[:80]   = {answer[:80]}")
        print(f"ttft={ttft} total={total} chars={chars} tps={tps}")
        print(f"console_errors = {errors}")
        print(f"screenshot    = {os.path.join(OUT, 'demo_chat.png')}")

        ok = bool(answer.strip()) and not errors and "ms" in total and int(chars) > 0
        browser.close()
        print("E2E", "PASS" if ok else "FAIL")
        return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
