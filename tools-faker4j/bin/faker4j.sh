#!/bin/sh

JAVA_CMD=`which java`
JAVA_HOME=`dirname "$JAVA_CMD"`

CLP=.
CLP=$CLP:./lib/commons-logging-1.0.4.jar:./target/tools-faker4j-0.3-SNAPSHOT.jar

export JAVA_HOME CLP

if [ -z $JAVA_HOME ]
then
    echo "Please, set JAVA_HOME before running this script."
    exit;
fi

echo ""
echo "generate test data..."
$JAVA_CMD -Xms256M -Xmx512M -classpath $CLP net.vitular.tools.faker4j.Main $@
echo "done."
