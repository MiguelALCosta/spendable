package com.app.spendable.presentation.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.app.spendable.databinding.BottomSheetChoiceBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class ChoiceBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val CHOICES_KEY = "CHOICES_KEY"
        private const val SELECTED_CHOICES_KEY = "SELECTED_CHOICES_KEY"
        private const val CALLBACK_KEY = "CALLBACK_KEY"
        const val TAG = "ChoiceBottomSheet"

        fun build(
            choices: List<SelectableChoiceComponent.Choice>,
            selectedChoiceId: String?,
            onSelection: (SelectableChoiceComponent.Choice) -> Unit
        ) =
            ChoiceBottomSheet().apply {
                val args = Bundle()
                args.putSerializable(CHOICES_KEY, choices as Serializable)
                selectedChoiceId?.let { args.putSerializable(SELECTED_CHOICES_KEY, it) }
                args.putSerializable(CALLBACK_KEY, onSelection as Serializable)
                arguments = args
            }

    }

    private var binding: BottomSheetChoiceBinding? = null

    private var onSelection: ((SelectableChoiceComponent.Choice) -> Unit)? = null
    private var choices: List<SelectableChoiceComponent.Choice>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetChoiceBinding.inflate(inflater, container, false)
        onSelection =
            arguments?.getSerializable(CALLBACK_KEY) as? (SelectableChoiceComponent.Choice) -> Unit

        choices =
            arguments?.getSerializable(CHOICES_KEY) as? List<SelectableChoiceComponent.Choice>
        val selectedChoiceId = arguments?.getSerializable(SELECTED_CHOICES_KEY) as? String

        choices?.forEachIndexed { i, choice ->
            val choiceComponent = SelectableChoiceComponent(requireContext()).apply {
                setup(choice)
                isSelected = choice.id == selectedChoiceId
                setOnClickListener { _ -> selectChoice(i) }
                layoutParams = GridLayout.LayoutParams().apply {
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                }
            }
            binding?.grid?.addView(choiceComponent)
        }

        return binding!!.root
    }

    private fun selectChoice(i: Int) {
        choices?.getOrNull(i)?.let { onSelection?.invoke(it) }
        dismiss()
    }

}