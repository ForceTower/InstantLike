package com.forcetower.likesview.ui.media

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.databinding.ItemMediaDetailedBinding

class MediaDetailsAdapter : PagedListAdapter<InstagramMedia, MediaDetailsAdapter.MediaHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(parent.inflate(R.layout.item_media_detailed))
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.binding.media = getItem(position)
    }

    inner class MediaHolder(val binding : ItemMediaDetailedBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem == newItem
        }
    }
}
