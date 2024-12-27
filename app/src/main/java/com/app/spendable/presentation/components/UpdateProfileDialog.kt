package com.app.spendable.presentation.components

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.widget.GridLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.app.spendable.R
import com.app.spendable.databinding.DialogUpdateProfileBinding
import com.app.spendable.domain.Avatar
import com.app.spendable.domain.settings.AppCurrency
import com.app.spendable.presentation.toIconResource
import com.app.spendable.utils.IStringsManager
import com.app.spendable.utils.PriceUtils
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class UpdateProfileDialog : DialogFragment() {

    companion object {

        fun build(
            initialState: State,
            currency: AppCurrency,
            onUpdate: (StateUpdate) -> Unit,
            onDismiss: () -> Unit
        ): UpdateProfileDialog {
            return UpdateProfileDialog().apply {
                this.state = initialState
                this.currency = currency
                this.onUpdate = onUpdate
                this.onDismiss = onDismiss
            }
        }

    }

    data class State(
        val totalBudget: BigDecimal,
        val username: String,
        val avatar: Avatar,
        val points: Int
    )

    data class StateUpdate(
        val totalBudget: BigDecimal,
        val username: String,
        val avatar: Avatar
    )

    @Inject
    lateinit var stringsManager: IStringsManager

    private var state: State? = null
    private var currency = AppCurrency.EUR
    private var onUpdate: ((StateUpdate) -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null

    private var selectedAvatar: Avatar? = null

    private lateinit var binding: DialogUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            onUpdate = null
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateProfileBinding.inflate(layoutInflater)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        setupView()

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    private fun setupView() {
        binding.close.setOnClickListener { dismiss() }
        binding.updateButton.setOnClickListener {
            onInputLoseFocus()
            val amountText = binding.amountInput.editText?.text?.toString()
                ?: return@setOnClickListener
            val state = StateUpdate(
                totalBudget = PriceUtils.Parse.fromAmount(amountText),
                username = binding.nameInput.editText?.text?.toString()
                    ?: return@setOnClickListener,
                avatar = selectedAvatar ?: return@setOnClickListener
            )
            onUpdate?.invoke(state)
            dismiss()
        }
        binding.amountInput.apply {
            val amountText = state?.totalBudget?.let { PriceUtils.Format.toAmount(it) }
            amountText?.let { editText?.setText(it) }
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                blockInputDecimals(text?.toString())
                updateButtonState()
            })
            editText?.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onInputLoseFocus()
                }
            }
            setEndIconDrawable(currency.toIconResource())
            errorIconDrawable = null
        }
        binding.nameInput.apply {
            state?.username?.let { editText?.setText(it) }
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                updateButtonState()
            })
        }
        binding.points.text = state?.points?.let {
            stringsManager.getString(R.string.x_points).format(it)
        }

        state?.let {
            selectedAvatar = it.avatar
            Avatar.progressList.forEachIndexed { i, avatar ->
                val choiceComponent = AvatarChoiceComponent(requireContext()).apply {
                    val config = AvatarChoiceComponent.SetupConfig(
                        avatar = avatar,
                        userPoints = state?.points ?: 0,
                        selected = it.avatar == avatar
                    )
                    setup(config)
                    setOnClickListener { _ -> selectAvatar(i) }
                    layoutParams = GridLayout.LayoutParams().apply {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                        rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1)
                    }
                }
                binding.grid.addView(choiceComponent)
            }
        }

        updateButtonState()
    }

    private fun selectAvatar(index: Int) {
        selectedAvatar?.let { Avatar.progressList.indexOf(it) }?.let { oldIndex ->
            (binding.grid.getChildAt(oldIndex) as? AvatarChoiceComponent)
                ?.isSelected = false
        }

        (binding.grid.getChildAt(index) as? AvatarChoiceComponent)
            ?.isSelected = true

        selectedAvatar = Avatar.progressList.getOrNull(index)
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

    private fun updateButtonState() {
        val amount = binding.amountInput.editText?.text?.toString()
            ?.let { PriceUtils.Parse.fromAmount(it) } ?: BigDecimal("0")
        val username = binding.nameInput.editText?.text?.toString()
        binding.updateButton.isEnabled = amount > BigDecimal(0) && !username.isNullOrBlank()
    }

}