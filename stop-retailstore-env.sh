#!/bin/zsh

echo "Stopping GlassFish..."
$HOME/EnterpriseApps/glassfish7/bin/asadmin stop-domain

echo ""
echo "Stopping MySQL..."
sudo /usr/local/mysql/support-files/mysql.server stop
 
echo ""
echo "Stopping FakeSMTP if running..."
pkill -f fakeSMTP-2.0.jar 2>/dev/null

echo ""
echo "All services stopped."
