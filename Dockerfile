FROM openjdk:11-jre-slim-stretch

MAINTAINER Gregory TARDIVEL

ENV DEV_MODE 0

WORKDIR /app
COPY jserver-core/build/install/jserver-core /app

VOLUME /apps

ENTRYPOINT ["bin/jserver-core"]
