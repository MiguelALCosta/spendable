package com.app.spendable.presentation.wallet

import com.app.spendable.domain.wallet.IWalletInteractor

interface IWalletPresenter {
    fun bind(view: WalletView)
    fun unbind()
    fun refreshWalletInfo()
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

}