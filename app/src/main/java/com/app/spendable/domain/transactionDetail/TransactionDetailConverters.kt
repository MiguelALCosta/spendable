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

fun Transaction?.toForm(stringsManager: IStringsManager, currency: AppCurrency): TransactionForm {
    val selectedDateTime = this?.date?.let { DateUtils.Parse.fromDateTime(it) }
        ?: DateUtils.Provide.nowDevice()

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
        currency = currency
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
