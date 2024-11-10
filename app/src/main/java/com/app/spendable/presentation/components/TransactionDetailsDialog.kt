package com.app.spendable.presentation.components

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import androidx.fragment.app.DialogFragment
import com.app.spendable.R
import com.app.spendable.databinding.DialogTransactionDetailBinding
import com.app.spendable.presentation.toIcon
import com.app.spendable.presentation.toTitleRes
import com.app.spendable.presentation.wallet.TransactionType
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.toFormatedPrice
import java.math.BigDecimal
import java.time.LocalDateTime


data class TransactionDetailModel(
    val id: Int,
    val type: TransactionType,
    val title: String,
    val description: String? = null,
    val cost: BigDecimal,
    val date: LocalDateTime
)


class TransactionDetailsDialog : DialogFragment() {

    companion object {

        fun build(
            detailModel: TransactionDetailModel,
            onClose: (delete: Boolean) -> Unit
        ): TransactionDetailsDialog {
            return TransactionDetailsDialog().apply {
                this.detailModel = detailModel
                this.onClose = onClose
            }
        }

    }

    private var detailModel: TransactionDetailModel? = null
    private var onClose: ((delete: Boolean) -> Unit)? = null
    private var shouldDelete = false

    private lateinit var binding: DialogTransactionDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            onClose = null
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTransactionDetailBinding.inflate(layoutInflater)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        setupView()

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        onClose?.invoke(shouldDelete)
        super.onDismiss(dialog)
    }

    private fun setupView() {
        binding.close.setOnClickListener { dismiss() }
        binding.deleteButton.setOnClickListener {
            shouldDelete = true
            dismiss()
        }
        binding.title.text = detailModel?.title
        binding.cost.text = detailModel?.cost?.toFormatedPrice()
        binding.notes.text = detailModel?.description ?: getString(R.string.none)
        binding.categoryIcon.setImageResource(
            detailModel?.type?.toIcon() ?: R.drawable.ic_other_transaction
        )
        binding.category.text = getString(detailModel?.type?.toTitleRes() ?: R.string.other_label)
        binding.date.text = detailModel?.date?.let { DateUtils.Format.toDateTime(it) }
    }

}