package com.nat.scalaworkshop.repository.codec

import java.text.SimpleDateFormat

import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import java.util.Date

import scala.util.Try

class JavaDateCodec extends Codec[Date] {
  import JavaDateCodec._

  override def encode(writer: BsonWriter, value: Date, encoderContext: EncoderContext): Unit = {
    writer.writeString(dateToIsoString(value))
  }

  override def getEncoderClass: Class[Date] = classOf[Date]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): Date = {
    parseIsoDateString(reader.readString())
      .getOrElse(throw new InternalError("Unable to decode date from database"))
  }
}

object JavaDateCodec {
  val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  }

  def dateToIsoString(date: Date): String =
    localIsoDateFormatter.get().format(date)

  def parseIsoDateString(date: String): Option[Date] =
    Try{ localIsoDateFormatter.get().parse(date) }.toOption
}
