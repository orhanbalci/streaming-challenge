package net.orhanbalci

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl._

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig

import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.TextMessage

object Main extends App {
  println("Log http service started")

  implicit val system                     = ActorSystem("log-http-service")
  implicit val materializer: Materializer = ActorMaterializer()

  val config = system.settings.config.getConfig("akka.kafka.consumer")

  val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(System.getenv("KAFKA_SERVER"))
    .withGroupId("log-puller")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  case class LogStruct(time: String, logLevel: String, center: String, content: String)

  val logStream = Consumer
    .plainSource(consumerSettings, Subscriptions.topics("log_totals"))
    .map(consRecord => TextMessage(consRecord.value))

  implicit val context: ExecutionContextExecutor = system.dispatcher

  // log_stream onComplete {
  //   case Success(_)   => println("log pulling over"); system.terminate()
  //   case Failure(err) => println(err.toString); system.terminate()
  // }

  val route = pathEndOrSingleSlash {
    get {
      complete("log server running")
    }
  } ~
    path("logs") {
      get {
        extractUpgradeToWebSocket { upgrade =>
          complete(
            upgrade.handleMessagesWithSinkSource(
              Sink.ignore,
              logStream
            )
          )
        }
      }
    }

  Http().bindAndHandle(route, sys.env("HOSTNAME"), sys.env("PORT").toInt)
  println(s"Server online at http://${sys.env("HOSTNAME")}:${sys.env("PORT")}")

}
