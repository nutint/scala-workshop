package com.nat.scalaworkshop.repository.codec

import com.nat.scalaworkshop.model.Price
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}

class PriceCodec extends Codec[Price] {
  override def encode(writer: BsonWriter, value: Price, encoderContext: EncoderContext): Unit =
    writer.writeString(value.toString)

  override def getEncoderClass: Class[Price] = classOf[Price]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): Price =
    Price(reader.readString())
}
