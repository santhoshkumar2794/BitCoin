package com.zestworks.bitcoin.ui.model

sealed class Duration
object OneWeek : Duration()
object OneMonth : Duration()
object SixMonths : Duration()
object OneYear : Duration()