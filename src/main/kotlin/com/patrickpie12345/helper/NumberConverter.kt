package com.patrickpie12345.helper

import java.math.RoundingMode
import java.text.DecimalFormat

object NumberConverter {

    private val decimalFormat = DecimalFormat("#.00").apply {
        this.roundingMode = RoundingMode.CEILING
    }

    fun floatToDollarConversion(value: Float): Float {
        return decimalFormat.format(value).toFloat()
    }
}
