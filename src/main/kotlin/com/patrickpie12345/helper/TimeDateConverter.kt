package com.patrickpie12345.helper

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

enum class TimeInterval {
    DAY, WEEK, MONTH, YEAR
}

data class TimeToSearch(
    val numOf: Long,
    val timeInterval: TimeInterval
)

object TimeDateConverter {

    fun getBeginningDate(timeToSearch: TimeToSearch): String {
        val startInterval = when (timeToSearch.timeInterval) {
            TimeInterval.DAY -> OffsetDateTime.now().minusDays(timeToSearch.numOf)
            TimeInterval.WEEK -> OffsetDateTime.now().minusWeeks(timeToSearch.numOf)
            TimeInterval.MONTH -> OffsetDateTime.now().minusMonths(timeToSearch.numOf)
            TimeInterval.YEAR -> OffsetDateTime.now().minusYears(timeToSearch.numOf)
        }.toInstant()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.from(ZoneOffset.UTC))
        return "${formatter.format(startInterval)}"
    }
}
