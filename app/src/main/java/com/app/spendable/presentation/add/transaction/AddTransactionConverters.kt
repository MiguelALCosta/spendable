package com.app.spendable.presentation.add.transaction

import com.app.spendable.data.db.Transaction
import com.app.spendable.utils.DateUtils
import java.time.LocalDateTime

fun AddTransactionForm.toTransaction(): Transaction? {
    return Transaction(
        type = selectedCategory ?: return null,
        title = title?.ifBlank { null } ?: return null,
        cost = amount?.ifBlank { null } ?: return null,
        date = DateUtils.Format.toDateTime(LocalDateTime.of(date, time)),
        description = notes
    )
}