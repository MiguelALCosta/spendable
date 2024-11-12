package com.app.spendable.presentation.subscriptionDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.app.spendable.R
import com.app.spendable.databinding.FragmentSubscriptionDetailBinding
import com.app.spendable.domain.subscriptionDetail.SubscriptionForm
import com.app.spendable.presentation.common.CloseableView
import com.app.spendable.presentation.components.ChoiceBottomSheet
import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.utils.DateUtils
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

interface ISubscriptionDetailView {
    fun setupView(form: SubscriptionForm)
    fun close()
    fun setAmountErrorState(hasError: Boolean)
    fun setTitleErrorState(hasError: Boolean)
    fun setCategoryErrorState(hasError: Boolean)
    fun setSubCategoryErrorState(hasError: Boolean)
    fun showGenericError()
    fun showCancelButton()
}

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class SubscriptionDetailFragment(private val subscriptionId: Int? = null) : Fragment(),
    ISubscriptionDetailView {

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }

    private var _binding: FragmentSubscriptionDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: ISubscriptionDetailPresenter

    private var form: SubscriptionForm? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        presenter.bind(this)
        presenter.loadView(subscriptionId)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.unbind()
    }

    override fun setupView(form: SubscriptionForm) {
        this.form = form
        setupInputs()
        setupButton()
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

    override fun setSubCategoryErrorState(hasError: Boolean) {
        setFieldError(binding.subcategoryInput, hasError)
    }

    private fun setFieldError(field: TextInputLayout, hasError: Boolean) {
        if (field.visibility != View.VISIBLE) {
            return
        }
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
            editText?.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    onPriceInputLoseFocus()
                }
            }
            errorIconDrawable = null
        }

        binding.categoryInput.apply {
            updateCategoryInput(form?.selectedCategory)
            editText?.setOnClickListener {
                showChoiceBottomSheet(
                    form?.categories ?: emptyList(),
                    form?.selectedCategory,
                    onSelection = ::setSelectedCategory
                )
            }
            errorIconDrawable = null
        }

        binding.subcategoryInput.apply {
            updateSubcategoryInput(form?.selectedSubcategory)
            updateSubcategoryVisibility()
            editText?.setOnClickListener {
                val choices =
                    form?.selectedCategory?.let { form?.subcategories?.getOrDefault(it, null) }
                showChoiceBottomSheet(
                    choices ?: emptyList(),
                    form?.selectedCategory,
                    onSelection = ::setSelectedSubcategory
                )
            }
            errorIconDrawable = null
        }

        binding.titleInput.apply {
            form?.title?.let { editText?.setText(it) }
            updateTitleVisibility()
            editText?.addTextChangedListener(onTextChanged = { text, _, _, _ ->
                form?.title = text?.toString()
            })
            errorIconDrawable = null
        }

        binding.frequencyInput.apply {
            updateFrequencyInput(form?.selectedFrequency)
            editText?.setOnClickListener {
                showChoiceBottomSheet(
                    form?.frequencies ?: emptyList(),
                    form?.selectedFrequency,
                    onSelection = ::setSelectedFrequency
                )
            }
            errorIconDrawable = null
        }

        binding.dateInput.editText?.apply {
            form?.date?.let { setSelectedDate(it) }
            setOnClickListener {
                showDatePicker()
            }
        }
    }

    private fun setupButton() {
        binding.saveButton.setOnClickListener {
            onPriceInputLoseFocus()
            presenter.saveSubscription(subscriptionId, form)
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
            segments[1].substring(0..1)
        }

        if (!priceText.isNullOrEmpty()) {
            binding.priceInput.editText?.setText("$left.$right")
        }

    }

    private fun showDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
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

    private fun showChoiceBottomSheet(
        choices: List<SelectableChoiceComponent.Choice>,
        selectedChoice: String?,
        onSelection: (String) -> Unit
    ) {
        val modalBottomSheet = ChoiceBottomSheet.build(
            choices,
            selectedChoice,
            onSelection = { onSelection(it.id) }
        )
        activity?.let {
            modalBottomSheet.show(
                it.supportFragmentManager,
                ChoiceBottomSheet.TAG
            )
        }
    }

    private fun setSelectedCategory(choiceId: String?) {
        form?.selectedCategory = choiceId
        updateCategoryInput(choiceId)
        updateSubcategoryVisibility()
        setSelectedSubcategory(null)
    }

    private fun updateCategoryInput(choiceId: String?) {
        val choice = form?.categories?.firstOrNull { it.id == choiceId }
        binding.categoryInput.editText?.setText(choice?.label ?: "")
    }

    private fun updateSubcategoryVisibility() {
        setSubCategoryErrorState(false)
        if (form?.getActiveSubcategoryChoices() == null) {
            binding.subcategoryInput.visibility = View.GONE
        } else {
            binding.subcategoryInput.visibility = View.VISIBLE
        }
    }

    private fun updateTitleVisibility() {
        setTitleErrorState(false)
        if (form?.needsTitle() == true) {
            binding.titleInput.visibility = View.VISIBLE
        } else {
            binding.titleInput.visibility = View.GONE
        }
    }

    private fun setSelectedSubcategory(choiceId: String?) {
        form?.selectedSubcategory = choiceId
        updateSubcategoryInput(choiceId)
        form?.title = null
        binding.titleInput.editText?.setText("")
        updateTitleVisibility()
    }

    private fun updateSubcategoryInput(choiceId: String?) {
        val choice = form?.getActiveSubcategoryChoices()?.firstOrNull { it.id == choiceId }
        binding.subcategoryInput.editText?.setText(choice?.label ?: "")
    }

    private fun setSelectedFrequency(choiceId: String) {
        form?.selectedFrequency = choiceId
        updateFrequencyInput(choiceId)
    }

    private fun updateFrequencyInput(choiceId: String?) {
        val choice = form?.frequencies?.firstOrNull { it.id == choiceId }
        binding.frequencyInput.editText?.setText(choice?.label ?: "")
    }

    override fun showCancelButton() {
        binding.cancelButton.visibility = View.VISIBLE
        binding.cancelButton.setOnClickListener { presenter.cancelSubscription(subscriptionId) }
    }
}