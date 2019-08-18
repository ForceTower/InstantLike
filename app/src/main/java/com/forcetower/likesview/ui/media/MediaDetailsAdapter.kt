package com.forcetower.likesview.ui.media

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.databinding.ItemInstagramPureMediaBinding
import com.forcetower.likesview.databinding.ItemMediaDetailedBinding

class MediaDetailsAdapter : PagedListAdapter<InstagramMedia, MediaDetailsAdapter.MediaHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(parent.inflate(R.layout.item_media_detailed))
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    inner class MediaHolder(val binding : ItemMediaDetailedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(media: InstagramMedia?) {
            media ?: return
            binding.media = media
            binding.imageContentPager.adapter = InstagramMediaAdapter(media.contents.images)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem == newItem
        }
    }
}

class InstagramMediaAdapter(images: List<String>) : PagerAdapter() {
    private val list = mutableListOf<String>()
    init {
        list.addAll(images)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount() = list.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = container.inflate<ItemInstagramPureMediaBinding>(R.layout.item_instagram_pure_media)
        binding.pictureUrl = list[position]
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
