package com.nat.scalaworkshop.route

import java.text.SimpleDateFormat
import java.util.Date

import com.nat.scalaworkshop.model.{Price, Rate, SupportedCurrency}

import scala.util.Try

object JsonFormats {
  import spray.json.DefaultJsonProtocol._
  import spray.json._

  implicit def dateFormat: JsonFormat[Date] = new JsonFormat[Date] {
    override def write(obj: Date): JsValue = JsString(dateToIsoString(obj))
    override def read(json: JsValue): Date = json match {
      case JsString(rawDate) => parseIsoDateString(rawDate)
        .fold(deserializationError(s"Expected ISO Date format[yyyy-MM-dd'T'HH:mm:ss.SSSZ] eg:2001-07-04T12:08:56.235-0700, got $rawDate"))(identity)
      case other => deserializationError(s"Expected JsString, got $other")
    }

    private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
      override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    }

    private def dateToIsoString(date: Date) =
      localIsoDateFormatter.get().format(date)

    private def parseIsoDateString(date: String): Option[Date] =
      Try{ localIsoDateFormatter.get().parse(date) }.toOption
  }

  implicit def priceFormat: JsonFormat[Price] = new JsonFormat[Price] {
    override def write(obj: Price): JsValue = JsString(obj.toString)

    override def read(json: JsValue): Price = json match {
      case JsString(priceStr) => {
        Try(Price(priceStr))
          .fold(err => deserializationError(s"unable to parse $priceStr to Price ${err.getMessage}"), identity)
      }
      case other => deserializationError(s"Expected JsString, got $other")
    }

  }

  implicit def supportedCurrencyFormat: JsonFormat[SupportedCurrency] = new JsonFormat[SupportedCurrency] {
    override def read(json: JsValue): SupportedCurrency = json match {
      case JsString(strVal) => SupportedCurrency.apply(strVal)
      case _ => deserializationError(s"Decoding supported currency failed: Expected Js String")
    }

    override def write(obj: SupportedCurrency): JsValue = JsString(obj.toString)
  }
  implicit def rateFormat: JsonFormat[Rate] = jsonFormat2(Rate.apply)
}
