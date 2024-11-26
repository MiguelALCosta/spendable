package com.app.spendable.domain.transactionDetail

import com.app.spendable.data.db.Transaction
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toIcon
import com.app.spendable.presentation.toTitleRes
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset

fun Transaction?.toForm(stringsManager: IStringsManager, currency: AppCurrency): TransactionForm {
    val now = DateUtils.Provide.nowDevice()
    val selectedDateTime = this?.date?.let { DateUtils.Parse.fromDateTime(it) } ?: now

    return TransactionForm(
        amount = this?.cost,
        title = this?.title,
        categories = TransactionType.entries.map {
            SelectableChoiceComponent.Choice(
                it.name,
                stringsManager.getString(it.toTitleRes()),
                it.toIcon()
            )
        },
        selectedCategory = this?.type,
        date = selectedDateTime.toLocalDate(),
        time = selectedDateTime.toLocalTime(),
        notes = this?.description,
        currency = currency,
        minDatePickerMillis = YearMonth.from(selectedDateTime).atDay(1).atStartOfDay()
            .toInstant(ZoneOffset.UTC).toEpochMilli(),
        maxDatePickerMillis = YearMonth.from(selectedDateTime).atEndOfMonth().atStartOfDay()
            .toInstant(ZoneOffset.UTC).toEpochMilli(),
        mode = when {
            this == null -> ExpenseDetailMode.CREATE
            YearMonth.from(selectedDateTime) == YearMonth.from(now) -> ExpenseDetailMode.EDITABLE
            else -> ExpenseDetailMode.READ_ONLY
        }
    )
}

fun TransactionForm.toTransaction(): Transaction? {
    return Transaction(
        type = selectedCategory ?: return null,
        title = title?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null } ?: return null,
        date = DateUtils.Format.toDateTime(LocalDateTime.of(date, time)),
        description = notes
    )
}
