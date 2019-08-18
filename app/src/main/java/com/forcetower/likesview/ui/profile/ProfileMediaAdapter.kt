package com.forcetower.likesview.ui.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.databinding.ItemProfileHomeMediaBinding

class ProfileMediaAdapter : ListAdapter<InstagramMedia, ProfileMediaAdapter.MediaHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(parent.inflate(R.layout.item_profile_home_media))
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.binding.apply {
            media = getItem(position)
            executePendingBindings()
        }
    }

    inner class MediaHolder(
        val binding: ItemProfileHomeMediaBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem == newItem
        }
    }
}
