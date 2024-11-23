package com.app.spendable.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.spendable.R
import com.app.spendable.databinding.FragmentSettingsBinding
import com.app.spendable.presentation.components.ChoiceBottomSheet
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

interface ISettingsView {
    fun setupLanguageInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    )

    fun setupThemeInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    )

    fun setupCurrencyInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    )

    fun setSelectedLanguage(selectedChoice: SelectableChoiceComponent.Choice)
    fun setSelectedTheme(selectedChoice: SelectableChoiceComponent.Choice)
    fun setSelectedCurrency(selectedChoice: SelectableChoiceComponent.Choice)
    fun showMessage(message: String)
    fun refreshActivity()
}

@AndroidEntryPoint
class SettingsFragment : Fragment(), ISettingsView {

    @Inject
    lateinit var presenter: ISettingsPresenter

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var selectedLanguage: String? = null
    private var selectedTheme: String? = null
    private var selectedCurrency: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter.bind(this)
        presenter.loadView()
        binding.clearButton.setOnClickListener {
            showClearConfirmation()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setupLanguageInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    ) {
        binding.languageInput.apply {
            setSelectedLanguage(selectedChoice)
            editText?.setOnClickListener {
                showChoiceBottomSheet(
                    choices,
                    selectedLanguage,
                    onSelection = { presenter.handleLanguageSelection(it) }
                )
            }
            errorIconDrawable = null
        }
    }

    override fun setupThemeInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    ) {
        binding.themeInput.apply {
            setSelectedTheme(selectedChoice)
            editText?.setOnClickListener {
                showChoiceBottomSheet(
                    choices,
                    selectedTheme,
                    onSelection = { presenter.handleThemeSelection(it) }
                )
            }
            errorIconDrawable = null
        }
    }

    override fun setupCurrencyInput(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: SelectableChoiceComponent.Choice
    ) {
        binding.currencyInput.apply {
            setSelectedCurrency(selectedChoice)
            editText?.setOnClickListener {
                showChoiceBottomSheet(
                    choices,
                    selectedCurrency,
                    onSelection = { presenter.handleCurrencySelection(it) }
                )
            }
            errorIconDrawable = null
        }
    }

    private fun showChoiceBottomSheet(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoiceId: String?,
        onSelection: (SelectableChoiceComponent.Choice) -> Unit
    ) {
        activity?.let {
            ChoiceBottomSheet.build(choices, selectedChoiceId, onSelection)
                .show(it.supportFragmentManager, ChoiceBottomSheet.TAG)
        }
    }

    override fun setSelectedLanguage(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedLanguage = selectedChoice.id
        binding.languageInput.editText?.setText(selectedChoice.label)
    }

    override fun setSelectedTheme(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedTheme = selectedChoice.id
        binding.themeInput.editText?.setText(selectedChoice.label)
    }

    override fun setSelectedCurrency(selectedChoice: SelectableChoiceComponent.Choice) {
        selectedCurrency = selectedChoice.id
        binding.currencyInput.editText?.setText(selectedChoice.label)
    }

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showClearConfirmation() {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setIcon(R.drawable.ic_warning)
                .setTitle(getString(R.string.clear_data_confirmation_title))
                .setMessage(getString(R.string.clear_data_confirmation_message))
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes_clear)) { _, _ ->
                    presenter.clearAppData()
                }
                .show()
        }
    }

    override fun refreshActivity() {
        activity?.recreate()
    }

}