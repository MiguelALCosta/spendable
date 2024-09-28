package com.app.spendable.presentation.wallet

import java.math.BigDecimal

sealed interface WalletAdapterModel

data class WalletCardModel(val budget: BigDecimal, val spent: BigDecimal) : WalletAdapterModel



