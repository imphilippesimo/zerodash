#!/bin/sh

echo "The app will start after ${SLEEP}s ..." && sleep ${SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom  -jar -Dspring.profiles.active=${SPRING_ACTIVE_PROFILES} "${HOME}/zerodash.jar" "$@"


