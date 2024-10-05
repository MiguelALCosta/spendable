package com.app.spendable.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


fun <T : View> T.setRecyclerViewLayoutParams(): T {
    val newLayoutParams = RecyclerView.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    this.layoutParams = newLayoutParams
    return this

}