#!/bin/sh

JAVA_CMD=`which java`
JAVA_HOME=`dirname "$JAVA_CMD"`

CLP=.
CLP=$CLP:./lib/commons-logging-1.0.4.jar:./target/tools-stat-0.1-SNAPSHOT.jar

export JAVA_HOME CLP

if [ -z $JAVA_HOME ]
then
    echo "Please, set JAVA_HOME before running this script."
    exit;
fi

echo ""
echo "statistic sth..."
$JAVA_CMD -Xms256M -Xmx512M -classpath $CLP net.vitular.tools.stat.App $@
echo "done."

