package com.patrickpie12345.helper

import java.math.RoundingMode
import java.text.DecimalFormat

object NumberConverter {

    private val decimalFormat = DecimalFormat("#.##").apply {
        this.roundingMode = RoundingMode.CEILING
    }

    fun convertToDollarFormat(value: Float): Float {
        return decimalFormat.format(value).toFloat()
    }
}
