package com.app.spendable.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {

    object AvailableFormats {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm"
        const val WEEKDAY_DAY_MONTH_NAME = "eee, dd MMMM"
        const val FULL_MONTH_YEAR = "MMMM yyyy"
        const val YEAR_MONTH = "yyyy-MM"
        //const val ISO_DATE_TIME = "yyyy-MM-ddZHH:mm"
        //const val ISO_DATE = "yyyy-MM-dd"
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

        fun toFullMonthYear(yearMonth: YearMonth) =
            yearMonth.format(getFormatter(AvailableFormats.FULL_MONTH_YEAR))

        fun toYearMonth(yearMonth: YearMonth) =
            yearMonth.format(getFormatter(AvailableFormats.YEAR_MONTH))

        fun toMillis(dateTime: LocalDateTime) =
            dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()

        fun toMillis(date: LocalDate) =
            toMillis(date.atStartOfDay())

        fun toMillis(yearMonth: YearMonth) =
            toMillis(yearMonth.atDay(1))

    }

    object Parse {
        fun fromDateTime(dateTime: String) =
            LocalDateTime.parse(dateTime, getFormatter(AvailableFormats.DATE_TIME_FORMAT))

        fun fromDate(dateTime: String) =
            LocalDate.parse(dateTime, getFormatter(AvailableFormats.DATE_FORMAT))

        fun fromYearMonth(dateTime: String) =
            YearMonth.parse(dateTime, getFormatter(AvailableFormats.YEAR_MONTH))

        fun fromMillisToDateTime(millis: Long) =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)

        fun fromMillisToDate(millis: Long) =
            fromMillisToDateTime(millis).toLocalDate()

        fun fromMillisToYearMonth(millis: Long) =
            YearMonth.from(fromMillisToDateTime(millis))
    }

    object Provide {
        fun nowDevice() = ZonedDateTime.now().toLocalDateTime()

        fun nowUTC() = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime()

        fun deviceTimeZone() = ZoneOffset.systemDefault()

        fun inCurrentMonth(date: LocalDate): LocalDate {
            val currentMonth = YearMonth.from(nowDevice())
            return inMonth(currentMonth, date)
        }

        fun inMonth(yearMonth: YearMonth, date: LocalDate): LocalDate {
            val monthDiff = ChronoUnit.MONTHS.between(YearMonth.from(date), yearMonth)
            return date.plusMonths(monthDiff)
        }
    }

}