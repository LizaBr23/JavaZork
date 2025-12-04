@echo off
REM JavaZork Multiplayer Client Launcher (Windows)
REM Usage: run-client.bat [host] [port]

set HOST=%1
if "%HOST%"=="" set HOST=localhost

set PORT=%2
if "%PORT%"=="" set PORT=8080

echo Starting JavaZork Multiplayer Client...
echo Connecting to: %HOST%:%PORT%
echo.

mvn exec:java@run-client -Dexec.args="%HOST% %PORT%"
