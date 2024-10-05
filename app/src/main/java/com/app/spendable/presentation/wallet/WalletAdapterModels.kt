package com.app.spendable.presentation.wallet

import java.math.BigDecimal

sealed interface WalletAdapterModel

data class WalletCardModel(val budget: BigDecimal, val spent: BigDecimal) : WalletAdapterModel

data class HeaderModel(val text: String) : WalletAdapterModel

enum class TransactionType {
    DUMMY
}

data class TransactionItemModel(
    val type: TransactionType,
    val title: String,
    val subtitle: String? = null,
    val cost: BigDecimal
) : WalletAdapterModel