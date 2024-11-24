package com.app.spendable.presentation.wallet

import com.app.spendable.domain.wallet.IWalletInteractor
import java.math.BigDecimal

interface IWalletPresenter {
    fun bind(view: WalletView)
    fun unbind()
    fun refreshWalletInfo()
    fun updateTotalBudget(newValue: BigDecimal)
}

class WalletPresenter(
    private val interactor: IWalletInteractor
) : IWalletPresenter {

    private var view: WalletView? = null

    override fun bind(view: WalletView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun refreshWalletInfo() {
        interactor.getModels {
            view?.updateView(it)
        }
    }

    override fun updateTotalBudget(newValue: BigDecimal) {
        interactor.updateTotalBudget(newValue) {
            refreshWalletInfo()
        }
    }

}