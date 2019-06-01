FROM openjdk:8-jre-alpine

RUN mkdir /opt/app
WORKDIR /opt/app

COPY ./target/scala-2.11/log-transformer-assembly-0.0.1.jar ./
CMD java -jar log-transformer-assembly-0.0.1.jar
