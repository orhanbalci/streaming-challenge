FROM openjdk:8-jre-alpine

RUN mkdir /opt/app
WORKDIR /opt/app

COPY ./target/scala-2.12/log-http-service-assembly-0.0.1.jar ./
COPY ./akka.kafka.consumer ./

CMD java -jar log-http-service-assembly-0.0.1.jar
