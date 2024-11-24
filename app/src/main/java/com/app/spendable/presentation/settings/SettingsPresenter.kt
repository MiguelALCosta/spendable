package com.app.spendable.presentation.settings

import com.app.spendable.R
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.domain.settings.AppLanguage
import com.app.spendable.domain.settings.AppTheme
import com.app.spendable.domain.settings.ISettingsInteractor
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.toIconResource
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.toEnum
import java.util.Locale

interface ISettingsPresenter {
    fun bind(view: ISettingsView)
    fun unbind()
    fun loadView()
    fun handleLanguageSelection(selectedChoice: SelectableChoiceComponent.Choice)
    fun handleThemeSelection(selectedChoice: SelectableChoiceComponent.Choice)
    fun handleCurrencySelection(selectedChoice: SelectableChoiceComponent.Choice)
    fun clearAppData()
}

class SettingsPresenter(
    private val interactor: ISettingsInteractor,
    private val stringsManager: IStringsManager
) : ISettingsPresenter {

    private var view: ISettingsView? = null

    override fun bind(view: ISettingsView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun loadView() {
        val languageChoices = interactor.getAvailableLanguages().map { it.toChoice() }
        val selectedLanguage = interactor.getSelectedLanguage().toChoice()
        view?.setupLanguageInput(languageChoices, selectedLanguage)

        val themeChoices = interactor.getAvailableThemes().map { it.toChoice() }
        val selectedTheme = interactor.getSelectedTheme().toChoice()
        view?.setupThemeInput(themeChoices, selectedTheme)

        val currencyChoices = interactor.getAvailableCurrencies().map { it.toChoice() }
        val selectedCurrency = interactor.getSelectedCurrency().toChoice()
        view?.setupCurrencyInput(currencyChoices, selectedCurrency)
    }

    private fun AppLanguage.toChoice() =
        SelectableChoiceComponent.Choice(
            id = name,
            label = when (this) {
                AppLanguage.SYSTEM -> stringsManager.getString(R.string.follow_system)
                else -> Locale(name).displayName.replaceFirstChar(Char::uppercase)
            },
            icon = when (this) {
                AppLanguage.SYSTEM -> R.drawable.ic_system
                else -> null
            }
        )

    private fun AppTheme.toChoice() =
        SelectableChoiceComponent.Choice(
            id = name,
            label = when (this) {
                AppTheme.SYSTEM -> stringsManager.getString(R.string.follow_system)
                AppTheme.LIGHT -> stringsManager.getString(R.string.light_theme)
                AppTheme.DARK -> stringsManager.getString(R.string.dark_theme)
            },
            icon = when (this) {
                AppTheme.SYSTEM -> R.drawable.ic_system
                AppTheme.LIGHT -> R.drawable.ic_light_mode
                AppTheme.DARK -> R.drawable.ic_dark_mode
            }
        )

    private fun AppCurrency.toChoice() =
        SelectableChoiceComponent.Choice(
            id = name,
            label = when (this) {
                AppCurrency.EUR -> stringsManager.getString(R.string.euro)
                AppCurrency.DOLLAR -> stringsManager.getString(R.string.dollar)
                AppCurrency.POUND -> stringsManager.getString(R.string.pound)
            },
            icon = toIconResource(),
        )

    override fun handleLanguageSelection(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedChoice.id.toEnum<AppLanguage>()?.let {
            interactor.updateAppLanguage(it)
        }
        view?.setSelectedLanguage(selectedChoice)
        view?.refreshActivity()
    }

    override fun handleThemeSelection(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedChoice.id.toEnum<AppTheme>()?.let {
            interactor.updateAppTheme(it)
        }
        view?.setSelectedTheme(selectedChoice)
    }

    override fun handleCurrencySelection(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedChoice.id.toEnum<AppCurrency>()?.let {
            interactor.updateAppCurrency(it)
        }
        view?.setSelectedCurrency(selectedChoice)
    }

    override fun clearAppData() {
        interactor.clearAppData {
            view?.showMessage(stringsManager.getString(R.string.data_cleared_message))
        }
    }
}