package net.orhanbalci.logpusher

import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.{to_json, struct}

object Main extends App {
  val sparkSession = SparkSession
    .builder()
    .master("local")
    .appName("log_pusher")
    .getOrCreate();
  import sparkSession.implicits._
  val schemaType = StructType(
    Array(
      StructField("timestamp", StringType),
      StructField("log_level", StringType),
      StructField("center", StringType),
      StructField("content", StringType)
    )
  )

  case class LogStruct(time: String, logLevel: String, center: String, content: String)

  val logStream = sparkSession.readStream
    .text("./logs/")
    .map(row => {
      val cols = row.getString(0).split("\\s+").map(s => s.trim())
      LogStruct(s"${cols(0)} ${cols(1)}", cols(2), cols(3), cols(4))
    })
    .select(to_json(struct("time", "logLevel", "center", "content")).alias("value"));

  val query = logStream.writeStream.format("kafka")
    .option("kafka.bootstrap.servers",sys.env("KAFKA_SERVER"))
    .option("topic","logs")
    .option("checkpointLocation","./checkpoint")
    .start()
  query.awaitTermination()
}
