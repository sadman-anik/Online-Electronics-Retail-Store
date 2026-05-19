#!/bin/zsh

set -e

MYSQL_SERVER="/usr/local/mysql/support-files/mysql.server"
GLASSFISH_ASADMIN="$HOME/EnterpriseApps/glassfish7/bin/asadmin"
FAKESMTP_JAR="fakeSMTP-2.0.jar"

echo "Stopping GlassFish..."
# Continue shutdown even if a service is already stopped.
"$GLASSFISH_ASADMIN" stop-domain || true

echo ""
echo "Stopping MySQL..."
sudo "$MYSQL_SERVER" stop || true

echo ""
echo "Stopping FakeSMTP if running..."
pkill -f "$FAKESMTP_JAR" 2>/dev/null || true

echo ""
echo "All services stopped."
