package com.app.spendable.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {

    object AvailableFormats {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    private fun getFormatter(pattern: String) =
        DateTimeFormatter.ofPattern(pattern)

    object Format {

        fun toDate(date: LocalDate) =
            date.format(getFormatter(AvailableFormats.DATE_FORMAT))

    }

}