package com.nat.scalaworkshop.model

import scala.util.Try

case class Price(
  amount: BigDecimal,
  currency: SupportedCurrency
) {
  override def toString: String = s"$amount $currency"
  /**
    * Convert money from current currency to target currency based on target currency
    * @param targetCurrency
    * @param rates
    * @return
    */
  def convert(targetCurrency: SupportedCurrency, rates: List[Rate]): Try[Price] = Try {
    (rates.find(_.currency==this.currency), rates.find(_.currency==targetCurrency)) match {
      case (Some(currentRate), Some(targetRate)) => {
        List(currentRate, targetRate)
          .filter(_.toThb <= 0) match {
          case Nil => {
            // Step 1: Convert to THB
            val thbRate = this.amount * currentRate.toThb
            // Step 2: Convert to target currency
            val tRate = thbRate / targetRate.toThb
            Price(
              tRate,
              targetCurrency
            )
          }
          case hasZeroList => {
            throw new IllegalArgumentException(s"Unable to convert currency: These currencies contains zero conversion ${hasZeroList.mkString(", ")}")
          }
        }
      }
      case (cur, tar) => {
        val missingCurrencies: String = List(cur, tar)
          .zip(List(this.currency, targetCurrency))
          .flatMap {
            case (None, curr) => Some(curr)
            case _ => None
          }
          .map(_.toString)
          .mkString(", ")

        throw new IllegalArgumentException(s"Unable to convert currency: missing the following currency $missingCurrencies")
      }
    }
  }

  def *(qty: BigDecimal): Price = copy(amount = amount * qty)

  def -(rhs: Price): Price =
    if(currency == rhs.currency) copy(amount = amount - rhs.amount)
    else throw new IllegalArgumentException(s"Unable to calculate different currency ($currency, and ${rhs.currency}")

  def +(rhs: Price): Price =
    if(currency == rhs.currency) copy(amount = amount + rhs.amount)
    else throw new IllegalArgumentException(s"Unable to calculate different currency ($currency, and ${rhs.currency}")
}

object Price {
  // Price("39 thb")
  def apply(priceStr: String): Price = {
    val components: List[String] = priceStr.split(" ").toList.map(_.trim)
    components match {
      case amount :: currency :: Nil => toPrice(amount, currency)
      case amount :: Nil => toPrice(amount, "thb")
      case _ => throw new IllegalArgumentException(s"Unable to convert $priceStr to Price")
    }
  }

  def toPrice(amount: String, currency: String): Price = {
    Try(Price(
      BigDecimal.apply(amount),
      SupportedCurrency(currency)
    )).getOrElse(throw new IllegalArgumentException(s"Unable to convert $amount $currency to Price"))
  }
}
