package com.forcetower.likesview.ui.profile

import android.view.View
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import com.forcetower.likesview.core.extensions.getPixelsFromDp
import com.forcetower.likesview.core.extensions.truncate

@BindingAdapter("textNumberWithK")
fun numberWithK(tv: TextView, value: Int) {
    val text = when {
        value < 1000 -> value.toString()
        value in 1000..999999 -> "${(value / 1000.0).truncate()}k"
        else -> "${(value / 1000000.0).truncate()}M"
    }
    tv.text = text
}

@BindingAdapter("selectedProfileMargin")
fun selectedProfileMargin(view: View, selected: Boolean?) {
    val active = selected ?: false
    val context = view.context
    val margin = if (active) 8 else 0
    val pixel = context.getPixelsFromDp(margin).toInt()
    view.updatePadding(0, 0, 0, pixel)
}