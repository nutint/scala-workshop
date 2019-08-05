package com.nat.scalaworkshop.model


class SupportedCurrency(
  val currency: SupportedCurrency.SupportedCurrencyInt
) {
  override def toString = currency.toString
  def ==(rhs: SupportedCurrency): Boolean =
    currency == rhs.currency

  def !=(rhs: SupportedCurrency): Boolean =
    ! ==(rhs)
}

object SupportedCurrency {

  trait SupportedCurrencyInt
  case object Thb extends SupportedCurrencyInt {
    override def toString: String = "THB"
  }
  case object Eur extends SupportedCurrencyInt {
    override def toString: String = "EUR"
  }
  case object Jpy extends SupportedCurrencyInt {
    override def toString: String = "JPY"
  }
  case object Sgd extends SupportedCurrencyInt {
    override def toString: String = "SGD"
  }
  case object Usd extends SupportedCurrencyInt {
    override def toString: String = "USD"
  }
  case object AnyCurrency extends SupportedCurrencyInt {
    override def toString: String = "Any"
  }

  def apply(strValue: String): SupportedCurrency = new SupportedCurrency(strToCurrency(strValue))

  def strToCurrency(str: String): SupportedCurrencyInt = str.toLowerCase match {
    case "thb" => Thb
    case "eur"| "euro" => Eur
    case "jpy" => Jpy
    case "sgd" => Sgd
    case "usd" => Usd
    case "any" => AnyCurrency
    case _ => throw new IllegalArgumentException(s"Unsupported currency $str")
  }

}
