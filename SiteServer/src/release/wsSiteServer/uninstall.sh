#!/bin/sh

echo ""
echo "WEATHER STATION SITE SERVER UNINSTALLER"
echo "----------------------------------------------------"
echo ""

echo ""
echo "Stopping the wsssd service"

systemctl stop wsssd

echo "Deregistering the wsssd service"

rm /lib/systemd/system/wsssd.service
systemctl daemon-reload
systemctl disable wsssd

echo "Removing installed files"

rm -R /usr/bin/wsssd/
rm -R /usr/lib/wsssd/
rm -R /etc/wsssd/
rm -R /var/log/wsssd/

echo "Weather Statoin Site Server Removed Successfully"
