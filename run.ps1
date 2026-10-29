# 启动本地网关（Windows PowerShell）
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
$env:PYTHONIOENCODING = "utf-8"
python -m uvicorn gateway.app:app --host 127.0.0.1 --port 8960
