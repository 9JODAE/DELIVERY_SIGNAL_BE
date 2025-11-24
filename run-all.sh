#!/bin/bash
set -e

echo "==========================================="
echo "     🚀 Delivery-Signal Local Environment"
echo "==========================================="

ROOT_DIR=$(pwd)

# --- Load .env ---
if [ -f "$ROOT_DIR/.env" ]; then
    export $(grep -v '^#' "$ROOT_DIR/.env" | xargs)
else
    echo "❌ .env 파일이 없습니다. 루트 폴더에 .env 파일을 두세요."
    exit 1
fi

# --- Container Names ---
POSTGRES_CONTAINER="postgres-db"
REDIS_CONTAINER="redis"
RABBIT_CONTAINER="rabbitmq"   # 🐇 표준 네이밍
EUREKA_CONTAINER="eureka"
GATEWAY_CONTAINER="gateway"

# ======================================================
# 1️⃣ RabbitMQ (역할: 메시지 브로커)
# ======================================================
echo ""
echo "=== 1️⃣ RabbitMQ 실행 (5673 → 5672) ==="

if docker ps -a --format '{{.Names}}' | grep -Eq "^${RABBIT_CONTAINER}\$"; then
    echo "➡ RabbitMQ 컨테이너 발견 → 재시작"
    docker start "$RABBIT_CONTAINER"
else
    echo "➡ RabbitMQ 컨테이너 없음 → 새로 생성"
    docker run -d --hostname rabbitmq-host \
        --name "$RABBIT_CONTAINER" \
        -p 5673:5672 \
        -p 15673:15672 \
        -e RABBITMQ_DEFAULT_USER=guest \
        -e RABBITMQ_DEFAULT_PASS=guest \
        rabbitmq:3-management
fi

echo "➡ RabbitMQ 관리 콘솔: http://localhost:15673 (guest / guest)"

# ======================================================
# 2️⃣ Redis
# ======================================================
echo ""
echo "=== 2️⃣ Redis 실행 ==="

if docker ps -a --format '{{.Names}}' | grep -Eq "^${REDIS_CONTAINER}\$"; then
    echo "➡ Redis 컨테이너 재시작"
    docker start "$REDIS_CONTAINER"
else
    echo "➡ Redis 새로 생성"
    docker run -d --name "$REDIS_CONTAINER" -p "$REDIS_PORT":6379 redis:7
fi


# ======================================================
# 3️⃣ PostgreSQL
# ======================================================
echo ""
echo "=== 3️⃣ PostgreSQL 실행 ==="

if docker ps -a --format '{{.Names}}' | grep -Eq "^${POSTGRES_CONTAINER}\$"; then
    echo "➡ PostgreSQL 컨테이너 재시작"
    docker start "$POSTGRES_CONTAINER"
else
    echo "➡ PostgreSQL 새로 생성"
    docker run -d --name "$POSTGRES_CONTAINER" \
      -e POSTGRES_DB="$POSTGRES_DB" \
      -e POSTGRES_USER="$POSTGRES_USER" \
      -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD" \
      -p "$POSTGRES_PORT":5432 \
      postgres:15
fi

echo "➡ PostgreSQL 준비 대기 중..."
until docker exec -i "$POSTGRES_CONTAINER" pg_isready -U "$POSTGRES_USER" > /dev/null 2>&1; do
  sleep 1
done
echo "➡ PostgreSQL 준비 완료"


# ======================================================
# 4️⃣ 스키마 생성
# ======================================================
echo ""
echo "=== 4️⃣ PostgreSQL 스키마 생성 ==="

SCHEMAS=("$ORDER_SCHEMA" "$PRODUCT_SCHEMA" "$USER_SCHEMA" "$DELIVERY_SCHEMA" "$COMPANY_SCHEMA" "$HUB_SCHEMA" "$EXTERNAL_SCHEMA")

for SCHEMA in "${SCHEMAS[@]}"
do
  if [ -n "$SCHEMA" ]; then
    echo "➡ 스키마 생성: $SCHEMA"
    docker exec -i "$POSTGRES_CONTAINER" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
      -c "CREATE SCHEMA IF NOT EXISTS \"$SCHEMA\";"
  fi
done

echo ""
echo "==========================================="
echo "  🎉 모든 인프라 컨테이너 실행 완료!"
echo "-------------------------------------------"
echo "🐇 RabbitMQ   → localhost:5673 (관리: 15673)"
echo "🟥 Redis      → localhost:$REDIS_PORT"
echo "🐘 PostgreSQL → localhost:$POSTGRES_PORT"
echo "==========================================="
