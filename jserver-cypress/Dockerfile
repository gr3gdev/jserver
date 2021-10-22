FROM adoptopenjdk/openjdk15:alpine-jre

WORKDIR /app
COPY build/install/jserver-cypress /app

ENTRYPOINT ["bin/jserver-cypress"]