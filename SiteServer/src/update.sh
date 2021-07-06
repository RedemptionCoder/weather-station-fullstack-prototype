#!/bin/sh

# remove all class files
echo ""
echo "WEATHER STATION SITE SERVER UPDATER"
echo "----------------------------------------------------"
echo ""
echo "Now removing old .class files"
find ../src -name *.class -delete 

# compile the java sources
echo "Now compiling the java source"
javac -encoding cp1252 -cp "/usr/lib/wsssd/gson-2.8.6.jar:/usr/lib/wsssd/activation-1.1.jar:/usr/lib/wsssd/javax.mail.jar" Main.java cruxarina/weatherlink/*.java

# create the jar file
echo "Now creating the jar file"
jar -cmf manifest-linux.txt wsssd.jar Main.class cruxarina/weatherlink/*.class

# stop the wsssd service for updating
echo "Stopping the wsssd service"
systemctl stop wsssd

# copy the jar file to the usr/bin/wsssd directory
echo "Now copying the wsssd.jar to the /usr/bin/wsssd/ folder"
cp wsssd.jar /usr/bin/wsssd/

# start the wsssd service for updating
echo "Starting the wsssd service"
systemctl start wsssd

# wait for the wsssd service to start properly
echo "Waiting for wsssd service to start"
sleep 10

# check status of wsssd service
echo "Checking status of wsssd service"
systemctl status wsssd



