#!/bin/zsh

set -e

MYSQL_SERVER="/usr/local/mysql/support-files/mysql.server"
GLASSFISH_ASADMIN="$HOME/EnterpriseApps/glassfish7/bin/asadmin"
FAKESMTP_DIR="$HOME/EnterpriseApps/FakeSMTP"
FAKESMTP_JAR="fakeSMTP-2.0.jar"
MYSQL_USER=${MYSQL_USER:-root}

echo "Starting MySQL..."
sudo "$MYSQL_SERVER" start

echo ""
echo "Checking retailestoredb database..."
mysql -u "$MYSQL_USER" -p -e "CREATE DATABASE IF NOT EXISTS retailestoredb;"

echo ""
echo "Starting GlassFish..."
"$GLASSFISH_ASADMIN" start-domain

echo ""
echo "Starting FakeSMTP on port 2525..."
echo "Keep the FakeSMTP window open and click 'Start Server' if needed."

cd "$FAKESMTP_DIR"

java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
    -jar "$FAKESMTP_JAR"
