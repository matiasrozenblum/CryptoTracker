package com.example.cryptotracker.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.math.BigDecimal
import java.math.RoundingMode

fun ViewGroup.inflate(layout: Int): View = LayoutInflater.from(this.context).inflate(layout, this, false)

fun getTwoDigitsDouble(value: String): String = BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).toString()