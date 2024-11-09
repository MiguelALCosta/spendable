package com.app.spendable.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    object AvailableFormats {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"
        const val WEEKDAY_DAY_MONTH_NAME = "eee, dd MMMM"
    }

    private fun getFormatter(pattern: String) =
        DateTimeFormatter.ofPattern(pattern)

    object Format {

        fun toDate(date: LocalDate) =
            date.format(getFormatter(AvailableFormats.DATE_FORMAT))

        fun toTime(time: LocalTime) =
            time.format(getFormatter(AvailableFormats.TIME_FORMAT))

        fun toDateTime(dateTime: LocalDateTime) =
            dateTime.format(getFormatter(AvailableFormats.DATE_TIME_FORMAT))

        fun toWeekdayDayMonth(date: LocalDate) =
            date.format(getFormatter(AvailableFormats.WEEKDAY_DAY_MONTH_NAME))

    }

    object Parse {
        fun fromDateTime(dateTime: String) =
            LocalDateTime.parse(dateTime, getFormatter(AvailableFormats.DATE_TIME_FORMAT))

        fun fromDate(dateTime: String) =
            LocalDate.parse(dateTime, getFormatter(AvailableFormats.DATE_FORMAT))
    }

    object Provide {
        fun nowDevice() = ZonedDateTime.now().toLocalDateTime()
    }

}