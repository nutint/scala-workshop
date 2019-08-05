package com.nat.scalaworkshop.model

case class Rate(
  currency: SupportedCurrency,
  toThb: BigDecimal
)
