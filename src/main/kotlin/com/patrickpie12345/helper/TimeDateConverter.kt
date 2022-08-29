package com.patrickpie12345.helper

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

enum class TimeInterval {
    DAY, WEEK, MONTH, YEAR
}

data class TimeToSearch(
    val numOf: Long,
    val timeInterval: TimeInterval
)

object TimeDateConverter {

    private const val DEFAULT_WEEK: Long = 1
    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.from(ZoneOffset.UTC))

    fun getBeginningDate(timeToSearch: TimeToSearch?): String {
        val startInterval = if (timeToSearch != null) {
            when (timeToSearch.timeInterval) {
                TimeInterval.DAY -> OffsetDateTime.now().minusDays(timeToSearch.numOf)
                TimeInterval.WEEK -> OffsetDateTime.now().minusWeeks(timeToSearch.numOf)
                TimeInterval.MONTH -> OffsetDateTime.now().minusMonths(timeToSearch.numOf)
                TimeInterval.YEAR -> OffsetDateTime.now().minusYears(timeToSearch.numOf)
            }.toInstant()
        } else {
            OffsetDateTime.now().minusWeeks(DEFAULT_WEEK).toInstant()
        }
        return instantToDate(startInterval)
    }

    fun getDateRange(beginningDate: String): String {
        val todayDate = instantToDate(OffsetDateTime.now().toInstant())
        return "${parseToReadableDate(beginningDate)}-${parseToReadableDate(todayDate)}"
    }

    fun stringToOffsetDateTime(string: String): OffsetDateTime {
        val dateTime = LocalDateTime.parse(string, FORMATTER)
        return OffsetDateTime.of(dateTime, ZoneOffset.UTC)
    }

    private fun parseToReadableDate(date: String): String {
        val odt = OffsetDateTime.parse(date)
        val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        return dtf.format(odt)
    }

    private fun instantToDate(instant: Instant): String {
        return FORMATTER.format(instant)
    }
}
