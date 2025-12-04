@echo off
REM JavaZork Multiplayer Client Launcher - Direct Java (Windows)
REM Usage: run-client-direct.bat [host] [port]

set HOST=%1
if "%HOST%"=="" set HOST=localhost

set PORT=%2
if "%PORT%"=="" set PORT=8080

echo Starting JavaZork Multiplayer Client...
echo Connecting to: %HOST%:%PORT%
echo.

REM First compile the project
echo Compiling project...
call mvn compile -q

if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)

REM Run using java directly with the compiled classes
java -cp "target/classes;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.17.2\jackson-databind-2.17.2.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.17.2\jackson-core-2.17.2.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.17.2\jackson-annotations-2.17.2.jar" ZorkGame.networking.MultiplayerClient %HOST% %PORT%
