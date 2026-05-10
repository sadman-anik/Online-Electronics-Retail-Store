#!/bin/zsh

SCRIPT_DIR="${0:A:h}"

echo "Stopping services..."
"$SCRIPT_DIR/stop-retailstore-env.sh"

echo ""
echo "Starting services..."
"$SCRIPT_DIR/start-retailstore-env.sh"
