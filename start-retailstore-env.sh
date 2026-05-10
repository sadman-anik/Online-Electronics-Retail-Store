#!/bin/zsh

echo "Starting MySQL..."
sudo /usr/local/mysql/support-files/mysql.server start

echo ""
echo "Starting GlassFish..."
$HOME/EnterpriseApps/glassfish7/bin/asadmin start-domain

echo ""
echo "Starting FakeSMTP on port 2525..."
echo "Keep the FakeSMTP window open and click 'Start Server' if needed."

cd $HOME/EnterpriseApps/FakeSMTP

java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED \
-jar fakeSMTP-2.0.jar