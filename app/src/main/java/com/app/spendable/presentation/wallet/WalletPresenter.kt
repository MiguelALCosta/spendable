package com.app.spendable.presentation.wallet

import com.app.spendable.domain.wallet.IWalletInteractor
import com.app.spendable.presentation.components.UpdateProfileDialog

interface IWalletPresenter {
    fun bind(view: WalletView)
    fun unbind()
    fun refreshWalletInfo()
    fun requestDailyReward()
    fun updateProfile(profileStateUpdate: UpdateProfileDialog.StateUpdate)
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

    override fun requestDailyReward() {
        interactor.requestDailyReward()?.let { dailyReward ->
            // reward was given if not null
            view?.showDailyRewardPopup(dailyReward)
        }
    }

    override fun updateProfile(profileStateUpdate: UpdateProfileDialog.StateUpdate) {
        interactor.updateProfile(profileStateUpdate) {
            refreshWalletInfo()
        }
    }

}