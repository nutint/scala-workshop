package com.nat.scalaworkshop.repository.codec


import java.math.RoundingMode

import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.types.Decimal128

class BigDecimalScalaCodec extends Codec[scala.math.BigDecimal] {

  override def encode(writer: BsonWriter, value: scala.math.BigDecimal, encoderContext: EncoderContext): Unit =
    writer.writeDecimal128(new Decimal128(value.bigDecimal.setScale(2, RoundingMode.CEILING)))

  override def getEncoderClass: Class[scala.math.BigDecimal] = classOf[scala.math.BigDecimal]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): scala.math.BigDecimal = {
    reader.readDecimal128().bigDecimalValue()
  }
}