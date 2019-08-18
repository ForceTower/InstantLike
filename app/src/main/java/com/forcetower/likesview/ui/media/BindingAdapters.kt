package com.forcetower.likesview.ui.media

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.forcetower.likesview.core.model.values.InstagramMedia

@BindingAdapter("calculateMediaParams")
fun calculateMediaParams(view: View, media: InstagramMedia?) {
    val desiredWidth = media?.dimensionWidth ?: 1
    val desiredHeight = media?.dimensionHeight ?: 1

    val viewWidth = view.width
    val viewHeight = viewWidth / (desiredWidth / desiredHeight.toFloat())

    view.layoutParams = ConstraintLayout.LayoutParams(viewWidth, viewHeight.toInt())
}