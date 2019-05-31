package net.orhanbalci

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.slick.scaladsl._
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

import slick.jdbc.PostgresProfile.api._

object Main extends App {
  println("Log puller started")

  implicit val system                     = ActorSystem("kafka-consumer")
  implicit val materializer: Materializer = ActorMaterializer()

  implicit val session = SlickSession.forConfig("slick-postgres")
  system.registerOnTermination(session.close())

  val config = system.settings.config.getConfig("akka.kafka.consumer")

  val consumerSettings = ConsumerSettings(config, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(System.getenv("KAFKA_SERVER"))
    .withGroupId("log-puller")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  import session.profile.api._
  case class LogStruct(time: String, logLevel: String, center: String, content: String)

  val log_stream = Consumer
    .plainSource(consumerSettings, Subscriptions.topics("logs"))
    .map(consRecord => decode[LogStruct](consRecord.value))
    .runWith(
      Slick.sink(
        logStruct =>
          logStruct match {
            case Right(v) =>
              sqlu"INSERT INTO logs VALUES (${v.time}, ${v.logLevel}, ${v.center}, ${v.content})"
          }
      )
    )

  implicit val context: ExecutionContextExecutor = system.dispatcher

  log_stream onComplete {
    case Success(_)   => println("log pulling over"); system.terminate()
    case Failure(err) => println(err.toString); system.terminate()

  }
}
