package com.app.spendable.presentation.add.transaction

import com.app.spendable.presentation.components.SelectableChoiceComponent
import java.time.LocalDate
import java.time.LocalTime

data class AddTransactionForm(
    var amount: String?,
    var title: String?,
    val categories: List<SelectableChoiceComponent.Choice>,
    var selectedCategory: String?,
    var date: LocalDate,
    var time: LocalTime,
    var notes: String?
)