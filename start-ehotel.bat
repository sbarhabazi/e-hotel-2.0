@echo off
setlocal EnableExtensions

set "ROOT_DIR=%~dp0"
set "COMPOSE_FILE=%ROOT_DIR%docker-compose.yml"
set "ACTION=%~1"
set "APP_PORT=%EHOTEL_APP_PORT%"

if "%ACTION%"=="" set "ACTION=up"
if "%APP_PORT%"=="" set "APP_PORT=8080"

where docker >nul 2>&1
if errorlevel 1 (
  echo Error: 'docker' command not found.
  exit /b 1
)

docker info >nul 2>&1
if errorlevel 1 (
  echo Error: Docker daemon is not reachable. Start Docker and retry.
  exit /b 1
)

if not exist "%COMPOSE_FILE%" (
  echo Error: missing file %COMPOSE_FILE%
  exit /b 1
)

docker compose version >nul 2>&1
if %ERRORLEVEL%==0 (
  set "USE_PLUGIN=1"
) else (
  where docker-compose >nul 2>&1
  if errorlevel 1 (
    echo Error: docker compose plugin (or docker-compose) is required.
    exit /b 1
  )
  set "USE_PLUGIN=0"
)

if /I "%ACTION%"=="up" goto :up
if /I "%ACTION%"=="down" goto :down
if /I "%ACTION%"=="logs" goto :logs

echo Usage: start-ehotel.bat [up^|down^|logs]
exit /b 1

:up
echo Starting E-Hotel with Docker Compose...
if "%USE_PLUGIN%"=="1" (
  docker compose -f "%COMPOSE_FILE%" up -d --build
) else (
  docker-compose -f "%COMPOSE_FILE%" up -d --build
)
if errorlevel 1 exit /b 1
echo E-Hotel is starting. Open: http://localhost:%APP_PORT%
exit /b 0

:down
if "%USE_PLUGIN%"=="1" (
  docker compose -f "%COMPOSE_FILE%" down
) else (
  docker-compose -f "%COMPOSE_FILE%" down
)
exit /b %ERRORLEVEL%

:logs
if "%USE_PLUGIN%"=="1" (
  docker compose -f "%COMPOSE_FILE%" logs -f app
) else (
  docker-compose -f "%COMPOSE_FILE%" logs -f app
)
exit /b %ERRORLEVEL%
