FROM alpine

ENV DEV_MODE 0

RUN apk --update add --no-cache openjdk15-jre --repository=http://dl-cdn.alpinelinux.org/alpine/edge/testing

WORKDIR /app
COPY jserver-core/build/install/jserver-core /app

VOLUME /apps

ENTRYPOINT ["bin/jserver-core"]