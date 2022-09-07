package com.patrickpie12345.helper

import java.time.*
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

enum class TimeInterval {
    DAY, WEEK, MONTH, YEAR
}

data class ReadableDateRange(
    val startDate: String,
    val endDate: String
)

data class OffsetDateRange(
    val startOffsetDate: OffsetDateTime,
    val endOffsetDate: OffsetDateTime
)

data class FromNow(
    val numOf: Long,
    val timeInterval: TimeInterval
)

/**
 * TimeToSearch object allows fine graining date search either between:
 *  - fine-grained start and end dates in the form of MMM dd, yyyy defined as `dateRange`
 *  - a specific time range from the today's date defined as `fromNow`
 *
 *  if both parameters are omitted then the default search is 1 week from today
 */
data class TimeToSearch(
    val dateRange: ReadableDateRange? = null,
    val fromNow: FromNow? = null
)

/**
 * TimeDateConverter is crucial for parsing our dates into the two different types:
 *  - OffsetDateTime (ISO Date) in the format of yyyy-MM-dd'T'HH:mm:ss.SSS'Z (example: 2022-08-27 23:09:31.043061 +00:00)
 *     a. OffsetDateTime is what will be used to query our DB
 *  - Readable date in the format of MMM dd, yyyy (example: Aug 23, 2022)
 *     a. Readable date is the format that'll be sent in our responses for clients' to interpret
 *
 * Important:
 *  - `readableDateToOffsetDate()` method is crucial at processing the readable dates received from clients
 *     converting them into OffsetDateTime to be used for DB queries.
 * - `offsetDateToReadableDate()` method is needed to convert the OffsetDateTime needed for DB queries to now
 *     be user-friendly in our client responses.
 */
object TimeDateConverter {

    private const val DEFAULT_WEEK: Long = 1
    private val READABLE_DATE_FORMATTER = DateTimeFormatterBuilder().appendPattern("MMM d, yyyy").apply {
        parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
    }.toFormatter()

    fun getOffsetDateRange(timeToSearch: TimeToSearch?): OffsetDateRange {
        var startOffsetDate = OffsetDateTime.now().minusWeeks(DEFAULT_WEEK)
        var endOffsetDate = OffsetDateTime.now()

        if (timeToSearch != null) {
            if (timeToSearch.dateRange != null) {
                timeToSearch.dateRange.let { dateRange ->
                    startOffsetDate = readableDateToOffsetDate(dateRange.startDate)
                    endOffsetDate = readableDateToOffsetDate(dateRange.endDate)
                }
            } else if (timeToSearch.fromNow != null) {
                timeToSearch.fromNow.let { fromNow ->
                    startOffsetDate = when (fromNow.timeInterval) {
                        TimeInterval.DAY -> OffsetDateTime.now().minusDays(fromNow.numOf)
                        TimeInterval.WEEK -> OffsetDateTime.now().minusWeeks(fromNow.numOf)
                        TimeInterval.MONTH -> OffsetDateTime.now().minusMonths(fromNow.numOf)
                        TimeInterval.YEAR -> OffsetDateTime.now().minusYears(fromNow.numOf)
                    }
                }
            }
        }
        return OffsetDateRange(startOffsetDate, endOffsetDate)
    }

    fun getReadableDateRange(start: OffsetDateTime, ending: OffsetDateTime): ReadableDateRange {
        val startDate = offsetDateToReadableDate(start)
        val endDate = offsetDateToReadableDate(ending)
        return ReadableDateRange(startDate, endDate)
    }

    private fun offsetDateToReadableDate(offsetDateTime: OffsetDateTime): String {
        return READABLE_DATE_FORMATTER.format(offsetDateTime)
    }

    private fun readableDateToOffsetDate(date: String): OffsetDateTime {
        val dateTime = LocalDateTime.parse(date, READABLE_DATE_FORMATTER)
        return OffsetDateTime.of(dateTime, ZoneOffset.UTC)
    }
}
