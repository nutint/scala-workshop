package com.nat.scalaworkshop.config

sealed trait BuildConfig
case object BuildConfigDevelopment extends BuildConfig
case object BuildConfigProduction extends BuildConfig
