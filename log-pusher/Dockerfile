FROM openjdk:8-jre-alpine

RUN mkdir /opt/app
WORKDIR /opt/app

COPY ./target/scala-2.11/log-pusher-assembly-0.0.1.jar ./
CMD java -jar log-pusher-assembly-0.0.1.jar
