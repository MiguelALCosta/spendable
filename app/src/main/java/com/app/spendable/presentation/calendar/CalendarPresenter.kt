package com.app.spendable.presentation.calendar

import com.app.spendable.domain.calendar.ICalendarInteractor

interface ICalendarPresenter {
    fun bind(view: ICalendarView)
    fun unbind()
    fun refreshCalendarInfo()
}

class CalendarPresenter(
    private val interactor: ICalendarInteractor
) : ICalendarPresenter {

    private var view: ICalendarView? = null

    override fun bind(view: ICalendarView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun refreshCalendarInfo() {
        /*val models = listOf(
            HeaderModel("2024"),
            MonthCardModel(
                YearMonth.of(2024, 7),
                BigDecimal("1000"),
                BigDecimal("804.75"),
                AppCurrency.EUR
            ),
            MonthCardModel(
                YearMonth.of(2024, 6),
                BigDecimal("1000"),
                BigDecimal("540"),
                AppCurrency.EUR
            ),
            MonthCardModel(
                YearMonth.of(2024, 5),
                BigDecimal("1000"),
                BigDecimal("724"),
                AppCurrency.EUR
            ),
            MonthCardModel(
                YearMonth.of(2024, 4),
                BigDecimal("1000"),
                BigDecimal("1340.86"),
                AppCurrency.EUR
            ),
            HeaderModel("2023"),
            MonthCardModel(
                YearMonth.of(2023, 12),
                BigDecimal("1000"),
                BigDecimal("804.75"),
                AppCurrency.EUR
            ),
        )
        view?.updateView(models)*/
        interactor.getModels {
            view?.updateView(it)
        }
    }

}