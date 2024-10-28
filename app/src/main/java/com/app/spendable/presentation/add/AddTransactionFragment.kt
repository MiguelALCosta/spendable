package com.app.spendable.presentation.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.app.spendable.R
import com.app.spendable.databinding.FragmentAddTransactionBinding
import com.app.spendable.presentation.components.ChoiceBottomSheet
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.utils.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@SuppressLint("SetTextI18n")
class AddTransactionFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
        private const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
    }

    private var _binding: FragmentAddTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val choices = listOf(
        SelectableChoiceComponent.Choice("1", "1", R.drawable.ic_add),
        SelectableChoiceComponent.Choice("2", "2", R.drawable.ic_add),
        SelectableChoiceComponent.Choice("3", "3", R.drawable.ic_add),
        SelectableChoiceComponent.Choice("4", "4", R.drawable.ic_add),
        SelectableChoiceComponent.Choice("5", "5", R.drawable.ic_add),
        SelectableChoiceComponent.Choice("6", "6", R.drawable.ic_add)
    )
    private var selectedChoiceId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupInputs()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupInputs() {
        binding.dateInput.editText?.setOnClickListener {
            showDatePicker()
        }
        binding.timeInput.editText?.setOnClickListener {
            showTimePicker()
        }
        binding.priceInput.editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            blockPriceDecimals(text?.toString())
        })

        binding.priceInput.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                onPriceInputLoseFocus()
            }
        }
        binding.categoryInput.editText?.setOnClickListener {
            showCategoryChoiceBottomSheet()
        }
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
            setSelectedDate(it)
        }
        activity?.let { datePicker.show(it.supportFragmentManager, DATE_PICKER_TAG) }
    }

    private fun setSelectedDate(millis: Long) {
        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
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
            setSelectedTime(timePicker.hour, timePicker.minute)
        }
        activity?.let { timePicker.show(it.supportFragmentManager, TIME_PICKER_TAG) }
    }

    private fun setSelectedTime(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        binding.timeInput.editText?.setText(DateUtils.Format.toTime(time))
    }

    private fun showCategoryChoiceBottomSheet() {
        val modalBottomSheet = ChoiceBottomSheet.build(
            choices,
            selectedChoiceId,
            onSelection = {
                selectedChoiceId = it.id
                binding.categoryInput.editText?.setText(it.label)
            }
        )
        activity?.let {
            modalBottomSheet.show(
                it.supportFragmentManager,
                ChoiceBottomSheet.TAG
            )
        }
    }
}