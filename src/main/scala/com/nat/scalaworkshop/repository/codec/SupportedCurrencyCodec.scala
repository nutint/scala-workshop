package com.nat.scalaworkshop.repository.codec

import com.nat.scalaworkshop.model.SupportedCurrency
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}

class SupportedCurrencyCodec extends Codec[SupportedCurrency] {
  override def decode(reader: BsonReader, decoderContext: DecoderContext): SupportedCurrency =
    SupportedCurrency(reader.readString())

  override def getEncoderClass: Class[SupportedCurrency] = classOf[SupportedCurrency]

  override def encode(writer: BsonWriter, value: SupportedCurrency, encoderContext: EncoderContext): Unit =
    writer.writeString(value.toString)
}
