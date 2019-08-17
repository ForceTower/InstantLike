package com.forcetower.likesview.ui.shared

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("clipToCircle")
fun clipToCircle(view: View, clip: Boolean) {
    view.clipToOutline = clip
    view.outlineProvider = if (clip) CircularOutlineProvider else null
}