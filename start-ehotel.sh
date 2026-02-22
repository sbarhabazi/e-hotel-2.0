#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd -P)"

CONTAINER_NAME="${EHOTEL_PG_CONTAINER:-ehotel-postgres}"
DB_PORT="${EHOTEL_PG_PORT:-5433}"
DB_NAME="${EHOTEL_DB_NAME:-db_hotel}"
DB_USER="${EHOTEL_DB_USER:-postgres}"
DB_PASSWORD="${EHOTEL_DB_PASSWORD:-password}"
DB_IMAGE="${EHOTEL_PG_IMAGE:-postgres:16}"
SCHEMA_FILE="${EHOTEL_SCHEMA_FILE:-$ROOT_DIR/schema.sql}"
OS_NAME="$(uname -s 2>/dev/null | tr '[:upper:]' '[:lower:]')"
IS_WINDOWS=false

case "$OS_NAME" in
  mingw*|msys*|cygwin*)
    IS_WINDOWS=true
    ;;
esac

require_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Erreur: commande '$1' introuvable." >&2
    exit 1
  fi
}

require_cmd docker

if ! docker info >/dev/null 2>&1; then
  echo "Erreur: Docker est installe mais le daemon n'est pas accessible." >&2
  echo "Demarre Docker Desktop (ou le service docker) puis relance." >&2
  exit 1
fi

if [ ! -f "$ROOT_DIR/EHotel/mvnw" ] && [ ! -f "$ROOT_DIR/EHotel/mvnw.cmd" ]; then
  echo "Erreur: fichier introuvable: $ROOT_DIR/EHotel/mvnw (ou mvnw.cmd)." >&2
  exit 1
fi

container_exists="$(docker ps -a --filter "name=^/${CONTAINER_NAME}$" --format '{{.Names}}')"
container_running="$(docker ps --filter "name=^/${CONTAINER_NAME}$" --format '{{.Names}}')"

if [ -z "$container_exists" ]; then
  echo "Demarrage du conteneur PostgreSQL ($CONTAINER_NAME) sur le port $DB_PORT..."
  docker run -d \
    --name "$CONTAINER_NAME" \
    -e "POSTGRES_DB=$DB_NAME" \
    -e "POSTGRES_USER=$DB_USER" \
    -e "POSTGRES_PASSWORD=$DB_PASSWORD" \
    -p "$DB_PORT:5432" \
    "$DB_IMAGE" >/dev/null
elif [ -z "$container_running" ]; then
  echo "Demarrage du conteneur PostgreSQL existant ($CONTAINER_NAME)..."
  docker start "$CONTAINER_NAME" >/dev/null
else
  echo "Conteneur PostgreSQL deja en cours d'execution ($CONTAINER_NAME)."
fi

echo "Attente de PostgreSQL..."
until docker exec "$CONTAINER_NAME" pg_isready -U "$DB_USER" -d postgres >/dev/null 2>&1; do
  sleep 1
done

db_exists="$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d postgres -tAc "select 1 from pg_database where datname='${DB_NAME}'" | tr -d '[:space:]')"
if [ "$db_exists" != "1" ]; then
  echo "Creation de la base $DB_NAME..."
  docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d postgres -v ON_ERROR_STOP=1 -c "CREATE DATABASE ${DB_NAME};" >/dev/null
fi

schema_exists="$(docker exec "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -tAc "select to_regclass('public.hotel_chain') is not null" | tr -d '[:space:]')"
if [ "$schema_exists" != "t" ]; then
  if [ ! -f "$SCHEMA_FILE" ]; then
    echo "Erreur: schema SQL introuvable: $SCHEMA_FILE" >&2
    exit 1
  fi
  echo "Import de schema.sql..."
  docker exec -i "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1 < "$SCHEMA_FILE" >/dev/null
fi

echo "Demarrage de l'application sur http://localhost:8080 ..."
cd "$ROOT_DIR/EHotel"

if [ -f "$ROOT_DIR/EHotel/mvnw" ]; then
  SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}" \
  SPRING_DATASOURCE_USERNAME="$DB_USER" \
  SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD" \
  sh mvnw spring-boot:run
elif [ "$IS_WINDOWS" = true ] && [ -f "$ROOT_DIR/EHotel/mvnw.cmd" ]; then
  SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}" \
  SPRING_DATASOURCE_USERNAME="$DB_USER" \
  SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD" \
  cmd.exe //c mvnw.cmd spring-boot:run
else
  echo "Erreur: aucun wrapper Maven compatible trouve." >&2
  echo "Attendu: EHotel/mvnw ou EHotel/mvnw.cmd" >&2
  exit 1
fi
