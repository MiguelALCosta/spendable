package com.app.spendable.domain.transactionDetail

import com.app.spendable.domain.Transaction
import com.app.spendable.domain.TransactionCreationRequest
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toIcon
import com.app.spendable.presentation.toTitleRes
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import com.app.spendable.utils.toEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneOffset

fun Transaction?.toForm(
    stringsManager: IStringsManager,
    currency: AppCurrency
): TransactionForm {
    val now = DateUtils.Provide.nowDevice()
    val selectedDateTime = this?.date ?: now

    return TransactionForm(
        amount = this?.cost?.let { PriceUtils.Format.toAmount(it) },
        title = this?.title,
        categories = TransactionType.entries.map {
            SelectableChoiceComponent.Choice(
                it.name,
                stringsManager.getString(it.toTitleRes()),
                it.toIcon()
            )
        },
        selectedCategory = this?.type?.name,
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

fun TransactionForm.toCreationRequest(): TransactionCreationRequest? {
    return TransactionCreationRequest(
        type = selectedCategory?.toEnum<TransactionType>() ?: return null,
        title = title?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null }?.let { BigDecimal(it) } ?: return null,
        date = LocalDateTime.of(date, time),
        description = notes
    )
}

fun TransactionForm.toTransaction(id: Int): Transaction? {
    return Transaction(
        id = id,
        type = selectedCategory?.toEnum<TransactionType>() ?: return null,
        title = title?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null }?.let { BigDecimal(it) } ?: return null,
        date = LocalDateTime.of(date, time),
        description = notes
    )
}
