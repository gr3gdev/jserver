FROM openjdk:11.0-jre-slim

WORKDIR /app
COPY build/install/jserver-cypress /app

ENTRYPOINT ["bin/jserver-cypress"]