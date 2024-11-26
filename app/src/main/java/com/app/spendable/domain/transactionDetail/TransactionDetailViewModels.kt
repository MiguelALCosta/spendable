package com.app.spendable.domain.transactionDetail

import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.components.SelectableChoiceComponent
import java.time.LocalDate
import java.time.LocalTime

enum class ExpenseDetailMode {
    CREATE, EDITABLE, READ_ONLY
}

data class TransactionForm(
    var amount: String?,
    var title: String?,
    val categories: List<SelectableChoiceComponent.Choice>,
    var selectedCategory: String?,
    var date: LocalDate,
    var time: LocalTime,
    var notes: String?,
    val currency: AppCurrency,
    val minDatePickerMillis: Long,
    val maxDatePickerMillis: Long,
    val mode: ExpenseDetailMode
)