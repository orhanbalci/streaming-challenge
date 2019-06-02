# Log Streaming POC
This project aims to demonstrate log streaming capabilities of scala projects such as Spark, Akka-Streaming and Akka-Http
All components of the project are dockerized and can be run easily on your local machine.

# Prerequisites
-Install docker on your machine
-Install docker-compose
-Install psql or equivalent postgresql db query tool
-Install kafkacat or equivalent kafka querying tool
-Install any websocket client
-Install git client 

# How to run the systems
-Get the repository from github. 
```
git clone https://github.com/orhanbalci/streaming-challenge
```
-Run system using 
```
./up.sh
```
-You can inspect messages on kafka using kafkacat. There are two topics. First one is logs
which contains json structured raw log messages. Second one is log_totals which includes
10 minutes windowed log totals per log center. 

```
kafkacat -C -b localhost -t logs
kafkacat -C -b localhost -t log_totals
```

-You can inspect logs written to postgresql db using tsql. Default database name is log_db
and table name is logs
```
psql -h localhost -U postgres log_db
select * from logs;
```

# Components of the system

## log-generator : generates fake log files and writes them into folder ./logsthis folder is mounted on docker host also

## zookeeper : needed for kafka. Although kafka runs in single mode zookeeper is needed

## kafka : kafka message broker

## log-pusher : watches foolder changes on logs folder. Reads log reacords and pushes them to kafka on logs topic. This module uses Apache Spark framework structured streaming capabilities

## log-db : postgresql db. Default db is log_db. Logs are written into logs table

## log-puller : reads messages from kafka logs topic and records them on postgres db. uses akka-streams and alpakka kafka source plus alpakka slick database sink

## log-transformer : reads messages from kafka logs topics and groups them according to 10 minutes time window. This module uses spark structured streaming capabilities

## log-http-service : uses akka-http websocket technology to serve messages from kafka log_totals topic


