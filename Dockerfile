FROM python:3.12-slim

WORKDIR /app
ENV PYTHONUNBUFFERED=1 \
    PYTHONIOENCODING=utf-8 \
    UPSTREAM=mock

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY gateway ./gateway
COPY tools ./tools
COPY demo ./demo
COPY docs ./docs

EXPOSE 8960
CMD ["python", "-m", "uvicorn", "gateway.app:app", "--host", "0.0.0.0", "--port", "8960"]
