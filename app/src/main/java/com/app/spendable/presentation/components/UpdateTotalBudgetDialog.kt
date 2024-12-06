package com.app.spendable.presentation.components

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.app.spendable.databinding.DialogUpdateTotalBudgetBinding
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.toIconResource
import com.app.spendable.utils.PriceUtils
import java.math.BigDecimal


class UpdateTotalBudgetDialog : DialogFragment() {

    companion object {

        fun build(
            currentValue: BigDecimal?,
            currency: AppCurrency,
            onUpdate: (BigDecimal) -> Unit
        ): UpdateTotalBudgetDialog {
            return UpdateTotalBudgetDialog().apply {
                this.initialValue = currentValue
                this.currency = currency
                this.onUpdate = onUpdate
            }
        }

    }

    private var initialValue: BigDecimal? = null
    private var currency = AppCurrency.EUR
    private var onUpdate: ((BigDecimal) -> Unit)? = null

    private lateinit var binding: DialogUpdateTotalBudgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            onUpdate = null
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateTotalBudgetBinding.inflate(layoutInflater)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        setupView()

        return dialog
    }

    private fun setupView() {
        binding.close.setOnClickListener { dismiss() }
        binding.updateButton.setOnClickListener {
            onInputLoseFocus()
            binding.amountInput.editText?.text?.toString()?.let { amountText ->
                onUpdate?.invoke(PriceUtils.Parse.fromAmount(amountText))
            }
            dismiss()
        }
        binding.amountInput.apply {
            val amountText = initialValue?.let { PriceUtils.Format.toAmount(it) }
            amountText?.let { editText?.setText(it) }
            updateButtonState(amountText)
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                blockInputDecimals(text?.toString())
                updateButtonState(text?.toString())
            })
            editText?.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onInputLoseFocus()
                }
            }
            setEndIconDrawable(currency.toIconResource())
            errorIconDrawable = null
        }
    }

    private fun blockInputDecimals(text: String?) {
        val correctedAmount = text?.let { PriceUtils.Format.toCorrectedPartialAmount(it) }
        if (correctedAmount != text) {
            binding.amountInput.editText?.setText(correctedAmount)
            binding.amountInput.editText?.setSelection(correctedAmount?.length ?: 0)
        }
    }

    private fun onInputLoseFocus() {
        val text = binding.amountInput.editText?.text?.toString()
        if (!text.isNullOrEmpty()) {
            val a = PriceUtils.Format.toCorrectedAmount(text)
            binding.amountInput.editText?.setText(a)
        }
    }

    private fun updateButtonState(inputText: String?) {
        val amount = inputText?.let { PriceUtils.Parse.fromAmount(it) } ?: BigDecimal("0")
        binding.updateButton.isEnabled = amount > BigDecimal(0)
    }

}