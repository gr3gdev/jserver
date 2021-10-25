FROM adoptopenjdk/openjdk11

ENV DEV_MODE 0

WORKDIR /app
COPY jserver-core/build/install/jserver-core /app

VOLUME /apps

ENTRYPOINT ["bin/jserver-core"]