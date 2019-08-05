#!/bin/sh

ENTRY_VAR="${CONFIG_ENV:-"-Xmx1024M -Xms1024M -Xmn128M"}"

JMX_EXPOSE="${CONFIG_JMX:-"-Dcom.sun.management.jmxremote -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.port=8081 -Dcom.sun.management.jmxremote.rmi.port=8081 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"}"

java -Dconfig.resource=production.conf -Dpidfile.path=/dev/null ${ENTRY_VAR} ${JMX_EXPOSE} -server -XX:+UseG1GC -jar /app/server.jar
