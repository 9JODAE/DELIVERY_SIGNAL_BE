#!/bin/bash
set -e

# --- 프로젝트 루트 경로 ---
ROOT_DIR=$(pwd)

# --- 환경 변수 로드 ---
if [ -f "$ROOT_DIR/.env" ]; then
    export $(grep -v '^#' "$ROOT_DIR/.env" | xargs)
else
    echo ".env 파일을 찾을 수 없습니다. 루트에 .env 파일을 두세요."
    exit 1
fi

# --- 컨테이너 이름 ---
POSTGRES_CONTAINER_NAME="postgres-db"
REDIS_CONTAINER_NAME="redis"
EUREKA_CONTAINER_NAME="eureka"
GATEWAY_CONTAINER_NAME="gateway"

# --- 1️⃣ Eureka 서버 ---
echo "=== 1️⃣ Eureka 서버 ==="
cd "$ROOT_DIR/com.delivery-signal.eureka.server"
docker build -t eurekaserver:latest .
docker run -d --name "$EUREKA_CONTAINER_NAME" -p "$EUREKA_PORT":19090 eurekaserver:latest
cd "$ROOT_DIR"

# --- 2️⃣ Redis ---
echo "=== 2️⃣ Redis ==="
docker run -d --name "$REDIS_CONTAINER_NAME" -p "$REDIS_PORT":6379 redis:7

# --- 3️⃣ PostgreSQL + 스키마 ---
echo "=== 3️⃣ PostgreSQL 컨테이너 실행 ==="
docker run -d --name "$POSTGRES_CONTAINER_NAME" \
  -e POSTGRES_DB="$POSTGRES_DB" \
  -e POSTGRES_USER="$POSTGRES_USER" \
  -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD" \
  -p "$POSTGRES_PORT":5432 \
  postgres:15

echo "=== 4️⃣ 스키마 생성 ==="

# PostgreSQL 준비될 때까지 대기
echo "PostgreSQL 시작 대기 중..."
until docker exec -i "$POSTGRES_CONTAINER_NAME" pg_isready -U "$POSTGRES_USER" > /dev/null 2>&1; do
  sleep 1
done
echo "PostgreSQL 준비 완료!"

# 스키마 생성
for SCHEMA in $ORDER_SCHEMA $PRODUCT_SCHEMA $USER_SCHEMA $DELIVERY_SCHEMA $COMPANY_SCHEMA $HUB_SCHEMA $EXTERNAL_SCHEMA
do
  echo "생성: $SCHEMA"
  docker exec -i "$POSTGRES_CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
    -c "CREATE SCHEMA IF NOT EXISTS \"$SCHEMA\";"
done

# --- 5️⃣ Gateway 서비스 ---
echo "=== 5️⃣ Gateway 서비스 ==="
cd "$ROOT_DIR/com.delivery-signal.eureka.client.gateway"
./gradlew clean build -x test
docker build -t gateway .
docker run -d --name "$GATEWAY_CONTAINER_NAME" --env-file "$ROOT_DIR/.env" -p "$GATEWAY_PORT":80 gateway
cd "$ROOT_DIR"

echo "✅ 모든 컨테이너 실행 완료!"
