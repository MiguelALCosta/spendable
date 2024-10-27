package com.app.spendable.presentation.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.spendable.databinding.FragmentAddTransactionBinding
import com.app.spendable.utils.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.ZoneId

class AddTransactionFragment : Fragment() {

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }

    private var _binding: FragmentAddTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
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
}