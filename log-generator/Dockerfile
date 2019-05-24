FROM openjdk:8-jre-alpine

RUN mkdir /opt/app
WORKDIR /opt/app

COPY ./target/scala-2.12/log-generator-assembly-0.1.0-SNAPSHOT.jar ./

CMD java -jar log-generator-assembly-0.1.0-SNAPSHOT.jar
