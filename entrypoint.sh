#!/bin/sh

echo "The app is starting ..."
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom  -jar -Dspring.profiles.active=${SPRING_ACTIVE_PROFILES} "/zdapplication/zerodash.jar" "$@"


