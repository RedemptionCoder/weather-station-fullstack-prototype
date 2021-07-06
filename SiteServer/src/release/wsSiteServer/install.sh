#!/bin/sh

echo ""
echo "WEATHER STATION SITE SERVER INSTALLER"
echo "----------------------------------------------------"
echo ""

echo "Copying files..."

mkdir /usr/bin/wsssd/
cp bin/wsssd.jar /usr/bin/wsssd/
mkdir /usr/lib/wsssd/
cp lib/* /usr/lib/wsssd/
mkdir /etc/wsssd/
cp etc/wsssd/wsssd.conf /etc/wsssd/
mkdir /var/log/wsssd/

echo "Registering the wsssd service"

cp systemd/wsssd.service /lib/systemd/system/

systemctl daemon-reload
systemctl enable wsssd.service

echo "Starting the wsssd service"

systemctl start wsssd

# Wait for the wsssd to start and connect to app server
sleep 20

systemctl status wsssd

echo ""
echo "Weather Statoin Site Server Installed Successfully"
