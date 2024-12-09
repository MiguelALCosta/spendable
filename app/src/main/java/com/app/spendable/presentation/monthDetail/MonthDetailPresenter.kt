package com.app.spendable.presentation.monthDetail

import com.app.spendable.domain.monthDetail.IMonthDetailInteractor
import java.time.YearMonth

interface IMonthDetailPresenter {
    fun bind(view: IMonthDetailView)
    fun unbind()
    fun getMonthDetail(yearMonth: YearMonth)
}

class MonthDetailPresenter(
    private val interactor: IMonthDetailInteractor
) : IMonthDetailPresenter {

    private var view: IMonthDetailView? = null

    override fun bind(view: IMonthDetailView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun getMonthDetail(yearMonth: YearMonth) {
        interactor.getModels(yearMonth) {
            view?.updateView(it)
        }
    }

}