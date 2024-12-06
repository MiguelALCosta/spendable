package com.app.spendable.presentation.transactionDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.app.spendable.R
import com.app.spendable.databinding.FragmentTransactionDetailBinding
import com.app.spendable.domain.transactionDetail.ExpenseDetailMode
import com.app.spendable.domain.transactionDetail.TransactionForm
import com.app.spendable.presentation.common.CloseableView
import com.app.spendable.presentation.components.ChoiceBottomSheet
import com.app.spendable.presentation.toIconResource
import com.app.spendable.utils.DateUtils
import com.app.spendable.utils.PriceUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

interface AddTransactionView {
    fun setupView(form: TransactionForm)
    fun close()
    fun setAmountErrorState(hasError: Boolean)
    fun setTitleErrorState(hasError: Boolean)
    fun setCategoryErrorState(hasError: Boolean)
    fun showGenericError()
}

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class AddTransactionFragment(private val transactionId: Int? = null) : Fragment(),
    AddTransactionView {

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        private const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
    }

    private var _binding: FragmentTransactionDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: IAddTransactionPresenter


    private var form: TransactionForm? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter.bind(this)
        presenter.loadView(transactionId)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.unbind()
    }

    override fun setupView(form: TransactionForm) {
        this.form = form
        setupInputs()
        setupButtons()
    }

    override fun close() {
        (activity as? CloseableView)?.close()
    }

    override fun setAmountErrorState(hasError: Boolean) {
        setFieldError(binding.priceInput, hasError)
    }

    override fun setTitleErrorState(hasError: Boolean) {
        setFieldError(binding.titleInput, hasError)
    }

    override fun setCategoryErrorState(hasError: Boolean) {
        setFieldError(binding.categoryInput, hasError)
    }

    private fun setFieldError(field: TextInputLayout, hasError: Boolean) {
        if (hasError) {
            field.isErrorEnabled = true
            field.error = getString(R.string.required_field_error)
        } else {
            field.isErrorEnabled = false
            field.error = null
        }
    }

    override fun showGenericError() {
        Snackbar.make(binding.root, R.string.generic_error, Snackbar.LENGTH_LONG).show()
    }

    private fun setupInputs() {
        binding.priceInput.apply {
            form?.amount?.let { editText?.setText(it) }
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                blockPriceDecimals(text?.toString())
                form?.amount = text?.toString()
            })
            editText?.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onPriceInputLoseFocus()
                }
            }
            form?.currency?.let { setEndIconDrawable(it.toIconResource()) }
            errorIconDrawable = null
        }

        binding.titleInput.apply {
            form?.title?.let { editText?.setText(it) }
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                form?.title = text?.toString()
            })
            errorIconDrawable = null
        }

        binding.categoryInput.apply {
            form?.selectedCategory?.let { setSelectedCategory(it) }
            editText?.setOnClickListener {
                showCategoryChoiceBottomSheet()
            }
            errorIconDrawable = null
        }

        binding.dateInput.editText?.apply {
            form?.date?.let { setSelectedDate(it) }
            setOnClickListener {
                showDatePicker()
            }
        }

        binding.timeInput.editText?.apply {
            form?.time?.let { setSelectedTime(it) }
            setOnClickListener {
                showTimePicker()
            }
        }

        binding.notesInput.editText?.apply {
            form?.notes?.let { setText(it) }
            addTextChangedListener(onTextChanged = { text, _, _, _ ->
                form?.notes = text?.toString()
            })
        }
        setInputsEnabledState(form?.mode == ExpenseDetailMode.CREATE)
    }

    private fun setInputsEnabledState(enabled: Boolean) {
        listOf(
            binding.priceInput,
            binding.titleInput,
            binding.categoryInput,
            binding.dateInput,
            binding.timeInput,
            binding.notesInput
        ).forEach { it.isEnabled = enabled }
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            onPriceInputLoseFocus()
            presenter.saveTransaction(transactionId, form)
        }
        binding.editButton.setOnClickListener {
            binding.saveButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.GONE
            setInputsEnabledState(true)
        }
        binding.deleteButton.setOnClickListener { showDeleteConfirmation() }

        when (form?.mode) {
            ExpenseDetailMode.CREATE -> {
                binding.saveButton.visibility = View.VISIBLE
            }

            ExpenseDetailMode.EDITABLE -> {
                binding.editButton.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.VISIBLE
            }

            else -> {
                // do nothing
            }
        }
    }

    private fun blockPriceDecimals(text: String?) {
        val correctedAmount = text?.let { PriceUtils.Format.toCorrectedPartialAmount(it) }
        if (correctedAmount != text) {
            binding.priceInput.editText?.setText(correctedAmount)
            binding.priceInput.editText?.setSelection(correctedAmount?.length ?: 0)
        }
    }

    private fun onPriceInputLoseFocus() {
        val text = binding.priceInput.editText?.text?.toString()
        if (!text.isNullOrEmpty()) {
            val a = PriceUtils.Format.toCorrectedAmount(text)
            binding.priceInput.editText?.setText(a)
        }
    }

    private fun showDatePicker() {
        val constraints = CalendarConstraints.Builder()
            .also { builder ->
                form?.minDatePickerMillis?.let { builder.setStart(it) }
                form?.maxDatePickerMillis?.let { builder.setEnd(it) }
            }
            .build()
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setCalendarConstraints(constraints)
                .also { builder ->
                    form?.date?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                        ?.let { builder.setSelection(it) }
                }
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val date = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            setSelectedDate(date)
        }
        activity?.let { datePicker.show(it.supportFragmentManager, DATE_PICKER_TAG) }
    }

    private fun setSelectedDate(date: LocalDate) {
        form?.date = date
        binding.dateInput.editText?.setText(DateUtils.Format.toDate(date))
    }

    private fun showTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTitleText(getString(R.string.select_time))
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .also { builder ->
                    form?.time?.hour?.let { builder.setHour(it) }
                    form?.time?.minute?.let { builder.setMinute(it) }
                }
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val time = LocalTime.of(timePicker.hour, timePicker.minute)
            setSelectedTime(time)
        }
        activity?.let { timePicker.show(it.supportFragmentManager, TIME_PICKER_TAG) }
    }

    private fun setSelectedTime(time: LocalTime) {
        form?.time = time
        binding.timeInput.editText?.setText(DateUtils.Format.toTime(time))
    }

    private fun showCategoryChoiceBottomSheet() {
        val modalBottomSheet = ChoiceBottomSheet.build(
            form?.categories ?: emptyList(),
            form?.selectedCategory,
            onSelection = { setSelectedCategory(it.id) }
        )
        activity?.let {
            modalBottomSheet.show(
                it.supportFragmentManager,
                ChoiceBottomSheet.TAG
            )
        }
    }

    private fun setSelectedCategory(choiceId: String) {
        form?.categories
            ?.first { it.id == choiceId }
            ?.let { choice ->
                form?.selectedCategory = choice.id
                binding.categoryInput.editText?.setText(choice.label)
                binding.categoryInput.setStartIconDrawable(choice.icon ?: 0)
            }
    }

    private fun showDeleteConfirmation() {
        activity?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(getString(R.string.delete_transaction_confirmation_message))
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    presenter.deleteTransaction(transactionId)
                }
                .show()
        }
    }
}