package net.orhanbalci.logtransformer

import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions.{to_json, struct, from_json, to_timestamp, count, lit, window}

object Main extends App {
  val sparkSession = SparkSession
    .builder()
    .master("local")
    .appName("log-transformer")
    .getOrCreate();
  import sparkSession.implicits._
  val schemaType = StructType(
    Array(
      StructField("time", StringType),
      StructField("logLevel", StringType),
      StructField("center", StringType),
      StructField("content", StringType)
    )
  )

  val logStream = sparkSession.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", sys.env("KAFKA_SERVER"))
    .option("subscribe", "logs")
    .option("startingOffsets", "earliest")
    .load()
    .select(from_json($"value".cast("string"), schemaType) as "json_log")
    .select($"json_log.*")
    .withColumn("entry_time", to_timestamp($"time", "yyyy-MM-dd HH:mm:ss.SSS"))
    .withWatermark("entry_time", "1 second")
    .groupBy(window($"entry_time", "10 minutes"), $"center")
    .agg(count(lit(1)).as("log_count"))
    .select(to_json(struct($"window.start", $"window.end", $"center", $"log_count")) as "value")
    .writeStream
    .outputMode("update")
    .format("kafka")
    .option("kafka.bootstrap.servers", sys.env("KAFKA_SERVER"))
    .option("topic", "log_totals")
    .option("checkpointLocation", "./checkpoint")
    .start()

  // val query = logDf.writeStream.outputMode("append").format("console").start()
  logStream.awaitTermination()
}
