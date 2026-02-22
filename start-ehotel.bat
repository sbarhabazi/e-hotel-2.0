@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "ROOT_DIR=%~dp0"

if "%EHOTEL_PG_CONTAINER%"=="" set "CONTAINER_NAME=ehotel-postgres"
if not "%EHOTEL_PG_CONTAINER%"=="" set "CONTAINER_NAME=%EHOTEL_PG_CONTAINER%"

if "%EHOTEL_PG_PORT%"=="" set "DB_PORT=5433"
if not "%EHOTEL_PG_PORT%"=="" set "DB_PORT=%EHOTEL_PG_PORT%"

if "%EHOTEL_DB_NAME%"=="" set "DB_NAME=db_hotel"
if not "%EHOTEL_DB_NAME%"=="" set "DB_NAME=%EHOTEL_DB_NAME%"

if "%EHOTEL_DB_USER%"=="" set "DB_USER=postgres"
if not "%EHOTEL_DB_USER%"=="" set "DB_USER=%EHOTEL_DB_USER%"

if "%EHOTEL_DB_PASSWORD%"=="" set "DB_PASSWORD=password"
if not "%EHOTEL_DB_PASSWORD%"=="" set "DB_PASSWORD=%EHOTEL_DB_PASSWORD%"

if "%EHOTEL_PG_IMAGE%"=="" set "DB_IMAGE=postgres:16"
if not "%EHOTEL_PG_IMAGE%"=="" set "DB_IMAGE=%EHOTEL_PG_IMAGE%"

if "%EHOTEL_SCHEMA_FILE%"=="" (
  set "SCHEMA_FILE=%ROOT_DIR%schema.sql"
) else (
  set "SCHEMA_FILE=%EHOTEL_SCHEMA_FILE%"
)

where docker >nul 2>&1
if errorlevel 1 (
  echo Erreur: commande 'docker' introuvable.
  exit /b 1
)

docker info >nul 2>&1
if errorlevel 1 (
  echo Erreur: Docker est installe mais le daemon n'est pas accessible.
  echo Demarre Docker Desktop puis relance.
  exit /b 1
)

if not exist "%ROOT_DIR%EHotel\mvnw.cmd" (
  echo Erreur: fichier introuvable: %ROOT_DIR%EHotel\mvnw.cmd
  exit /b 1
)

set "CONTAINER_EXISTS="
for /f "delims=" %%i in ('docker ps -a --filter "name=^/%CONTAINER_NAME%$" --format "{{.Names}}"') do set "CONTAINER_EXISTS=%%i"

set "CONTAINER_RUNNING="
for /f "delims=" %%i in ('docker ps --filter "name=^/%CONTAINER_NAME%$" --format "{{.Names}}"') do set "CONTAINER_RUNNING=%%i"

if "%CONTAINER_EXISTS%"=="" (
  echo Demarrage du conteneur PostgreSQL (%CONTAINER_NAME%) sur le port %DB_PORT%...
  docker run -d --name "%CONTAINER_NAME%" -e "POSTGRES_DB=%DB_NAME%" -e "POSTGRES_USER=%DB_USER%" -e "POSTGRES_PASSWORD=%DB_PASSWORD%" -p "%DB_PORT%:5432" "%DB_IMAGE%" >nul
  if errorlevel 1 exit /b 1
) else if "%CONTAINER_RUNNING%"=="" (
  echo Demarrage du conteneur PostgreSQL existant (%CONTAINER_NAME%)...
  docker start "%CONTAINER_NAME%" >nul
  if errorlevel 1 exit /b 1
) else (
  echo Conteneur PostgreSQL deja en cours d'execution (%CONTAINER_NAME%).
)

echo Attente de PostgreSQL...
:wait_pg
docker exec "%CONTAINER_NAME%" pg_isready -U "%DB_USER%" -d postgres >nul 2>&1
if errorlevel 1 (
  timeout /t 1 /nobreak >nul
  goto wait_pg
)

set "DB_EXISTS="
for /f "delims= " %%i in ('docker exec "%CONTAINER_NAME%" psql -U "%DB_USER%" -d postgres -tAc "select 1 from pg_database where datname=''%DB_NAME%''"') do set "DB_EXISTS=%%i"
if not "%DB_EXISTS%"=="1" (
  echo Creation de la base %DB_NAME%...
  docker exec "%CONTAINER_NAME%" psql -U "%DB_USER%" -d postgres -v ON_ERROR_STOP=1 -c "CREATE DATABASE %DB_NAME%;" >nul
  if errorlevel 1 exit /b 1
)

set "SCHEMA_EXISTS="
for /f "delims= " %%i in ('docker exec "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -tAc "select to_regclass(''public.hotel_chain'') is not null"') do set "SCHEMA_EXISTS=%%i"
if /I not "%SCHEMA_EXISTS%"=="t" (
  if not exist "%SCHEMA_FILE%" (
    echo Erreur: schema SQL introuvable: %SCHEMA_FILE%
    exit /b 1
  )
  echo Import de schema.sql...
  type "%SCHEMA_FILE%" | docker exec -i "%CONTAINER_NAME%" psql -U "%DB_USER%" -d "%DB_NAME%" -v ON_ERROR_STOP=1 >nul
  if errorlevel 1 exit /b 1
)

echo Demarrage de l'application sur http://localhost:8080 ...
cd /d "%ROOT_DIR%EHotel"
set "SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:%DB_PORT%/%DB_NAME%"
set "SPRING_DATASOURCE_USERNAME=%DB_USER%"
set "SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%"
call mvnw.cmd spring-boot:run
exit /b %ERRORLEVEL%
