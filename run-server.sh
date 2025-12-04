#!/bin/bash
# JavaZork Multiplayer Server Launcher
# Usage: ./run-server.sh [port] [max_players]

PORT=${1:-8080}
MAX_PLAYERS=${2:-4}

echo "Starting JavaZork Multiplayer Server (Shared World)..."
echo "Port: $PORT"
echo "Max Players: $MAX_PLAYERS"
echo ""

mvn exec:java@run-server -Dexec.args="$PORT $MAX_PLAYERS"
