#!/bin/bash
# JavaZork Multiplayer Client Launcher
# Usage: ./run-client.sh [host] [port]

HOST=${1:-localhost}
PORT=${2:-8080}

echo "Starting JavaZork Multiplayer Client..."
echo "Connecting to: $HOST:$PORT"
echo ""

mvn exec:java -Dexec.mainClass="ZorkGame.networking.MultiplayerClient" -Dexec.args="$HOST $PORT"
