package net.orhanbalci

import better.files._
import File._
import java.io.{File => JFile}
import scala.util.Random

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import better.files.Dsl._

object Main extends App {
  println("Hello scala")

  val r            = Random
  var timeSeed     = Instant.now()
  var fileNameSeed = 0;

  var nextLogFile = File(s"./logs/${nextLogFileName}")
  mkdirs(file"./logs")
  nextLogFile.createIfNotExists()
  while (nextLogFile.size < 2 * 1000 * 100) {
    nextLogFile.append(
      f"${randomTimeStamp} ${randomLogLevel}%6s ${randomCenter}%9s ${randomLogContent(20)}"
    )
    nextLogFile.appendLine()
  }

  def nextLogFileName = { fileNameSeed += 1; s"log_${fileNameSeed}.txt" }
  def randomTimeStamp(): String = {
    timeSeed = Instant.ofEpochMilli(timeSeed.toEpochMilli() + r.nextInt(250));
    DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
      .withZone(ZoneId.systemDefault())
      .format(timeSeed)
  }

  def randomLogLevel(): String = {
    r.nextInt(5) match {
      case 0 => "INFO"
      case 1 => "WARN"
      case 2 => "FATAL"
      case 3 => "DEBUG"
      case 4 => "ERROR"
    }
  }

  def randomCenter(): String = {
    r.nextInt(5) match {
      case 0 => "Istanbul"
      case 1 => "Tokyo"
      case 2 => "Moscow"
      case 3 => "Beijing"
      case 4 => "London"
    }
  }

  def randomLogContent(contentLength: Int): String = {
    r.alphanumeric.take(contentLength).mkString
  }
}
