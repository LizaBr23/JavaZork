@echo off
REM JavaZork Multiplayer Server Launcher (Windows)
REM Usage: run-server.bat [port] [max_players]

set PORT=%1
if "%PORT%"=="" set PORT=8080

set MAX_PLAYERS=%2
if "%MAX_PLAYERS%"=="" set MAX_PLAYERS=4

echo Starting JavaZork Multiplayer Server (Shared World)...
echo Port: %PORT%
echo Max Players: %MAX_PLAYERS%
echo.

mvn exec:java@run-server -Dexec.args="%PORT% %MAX_PLAYERS%"
