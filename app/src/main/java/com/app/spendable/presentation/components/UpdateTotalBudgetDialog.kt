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
                onUpdate?.invoke(BigDecimal(amountText))
            }
            dismiss()
        }
        binding.amountInput.apply {
            initialValue?.let { editText?.setText(it.toString()) }
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                blockInputDecimals(text?.toString())
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
        val segments = text?.split(".") ?: emptyList()
        if ((segments.getOrNull(1)?.count() ?: 0) > 2) {
            val correctedDecimal = segments.get(1).substring(0..1)
            binding.amountInput.editText?.setText("${segments.get(0)}.$correctedDecimal")
            binding.amountInput.editText?.setSelection(
                binding.amountInput.editText?.text?.length ?: 0
            )
        }
    }

    private fun onInputLoseFocus() {
        val priceText = binding.amountInput.editText?.text?.toString()
        val segments = priceText?.split(".") ?: emptyList()

        val left = segments.getOrNull(0) ?: ""
        val right = if (segments.getOrNull(1) == null) {
            "00"
        } else {
            segments.get(1).substring(0..1)
        }

        if (!priceText.isNullOrEmpty()) {
            binding.amountInput.editText?.setText("$left.$right")
        }

    }

}