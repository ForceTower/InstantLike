package com.forcetower.likesview.ui.shared

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.forcetower.likesview.GlideApp
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["imageUri", "placeholder", "clipCircle", "listener"], requireAll = false)
fun imageUri(imageView: ImageView, imageUri: Uri?, placeholder: Drawable?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    val circular = clipCircle ?: false
    var request = when (imageUri) {
        null -> {
            GlideApp.with(imageView)
                .load(placeholder)
        }
        else -> {
            GlideApp.with(imageView)
                .load(imageUri)
                .apply(RequestOptions().placeholder(placeholder))
        }
    }
    request = if (circular) {
        request.circleCrop()
    } else {
        request
    }

    if (listener != null) {
        request = request.listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                listener.onImageLoaded()
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                listener.onImageLoadFailed()
                return false
            }
        })
    }
    request.into(imageView)
}

@BindingAdapter(value = ["imageUrl", "placeholder", "clipCircle", "listener"], requireAll = false)
fun imageUrl(imageView: ImageView, imageUrl: String?, placeholder: Drawable?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    GlideApp.with(imageView).load(imageUrl).into(imageView)
}

interface ImageLoadListener {
    fun onImageLoaded()
    fun onImageLoadFailed()
}