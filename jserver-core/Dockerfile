FROM adoptopenjdk/openjdk15:alpine-jre

ENV DEV_MODE 0

WORKDIR /app
COPY build/install/jserver-core /app

VOLUME /apps

ENTRYPOINT ["bin/jserver-core"]