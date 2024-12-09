package com.app.spendable.presentation.monthDetail

import com.app.spendable.presentation.components.TransactionItemComponent
import com.app.spendable.presentation.wallet.SubscriptionListItemModel

sealed interface MonthDetailAdapterModel {

    data class Header(val text: String) : MonthDetailAdapterModel

    data class Transaction(
        val id: Int,
        val config: TransactionItemComponent.SetupConfig
    ) : MonthDetailAdapterModel

    data class SubscriptionsList(
        val items: List<SubscriptionListItemModel>
    ) : MonthDetailAdapterModel

}
