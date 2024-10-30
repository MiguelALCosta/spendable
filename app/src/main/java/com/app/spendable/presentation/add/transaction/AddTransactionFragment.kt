package com.app.spendable.presentation.add.transaction

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.app.spendable.R
import com.app.spendable.databinding.FragmentAddTransactionBinding
import com.app.spendable.presentation.add.AddView
import com.app.spendable.presentation.components.ChoiceBottomSheet
import com.app.spendable.utils.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

interface AddTransactionView {
    fun setupView(form: AddTransactionForm)
    fun closeAdd()
    fun setAmountErrorState(hasError: Boolean)
    fun setTitleErrorState(hasError: Boolean)
    fun setCategoryErrorState(hasError: Boolean)
    fun showGenericError()
}

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class AddTransactionFragment : Fragment(), AddTransactionView {

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        private const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
    }

    private var _binding: FragmentAddTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: IAddTransactionPresenter


    private var form: AddTransactionForm? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter.bind(this)
        presenter.loadView()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.unbind()
    }

    override fun setupView(form: AddTransactionForm) {
        this.form = form
        setupInputs()
        setupButton()
    }

    override fun closeAdd() {
        (activity as? AddView)?.closeAdd()
    }

    override fun setAmountErrorState(hasError: Boolean) {
        binding.priceInput.error = if (hasError) {
            getString(R.string.required_field_error)
        } else null
    }

    override fun setTitleErrorState(hasError: Boolean) {
        binding.titleInput.error = if (hasError) {
            getString(R.string.required_field_error)
        } else null
    }

    override fun setCategoryErrorState(hasError: Boolean) {
        binding.categoryInput.error = if (hasError) {
            getString(R.string.required_field_error)
        } else null
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
            editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    onPriceInputLoseFocus()
                }
            }
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
    }

    private fun setupButton() {
        binding.saveButton.setOnClickListener { presenter.saveTransaction(form) }
    }

    private fun blockPriceDecimals(text: String?) {
        val segments = text?.split(".") ?: emptyList()
        if ((segments.getOrNull(1)?.count() ?: 0) > 2) {
            val correctedDecimal = segments.get(1).substring(0..1)
            binding.priceInput.editText?.setText("${segments.get(0)}.$correctedDecimal")
            binding.priceInput.editText?.setSelection(
                binding.priceInput.editText?.text?.length ?: 0
            )
        }
    }

    private fun onPriceInputLoseFocus() {
        val priceText = binding.priceInput.editText?.text?.toString()
        val segments = priceText?.split(".") ?: emptyList()

        val left = segments.getOrNull(0) ?: ""
        val right = if (segments.getOrNull(1) == null) {
            "00"
        } else {
            segments.get(1).substring(0..1)
        }

        if (!priceText.isNullOrEmpty()) {
            binding.priceInput.editText?.setText("$left.$right")
        }

    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
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
            }
    }
}