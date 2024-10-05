package com.app.spendable.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.app.spendable.databinding.ComponentHeaderBinding

class HeaderComponent(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: ComponentHeaderBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ComponentHeaderBinding.inflate(inflater, this)
    }

    fun setup(text: String) {
        binding.label.text = text
    }

}
