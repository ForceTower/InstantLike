package com.forcetower.likesview.ui.profile

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.databinding.ItemProfileHomeMediaBinding
import com.forcetower.likesview.databinding.ItemProfileResumeBinding

class ProfileMediaAdapter(
    private val actions: ProfileActions
) : PagedListAdapter<InstagramMedia, ProfileMediaAdapter.HomeHolder>(DiffCallback) {

    var profile: InstagramProfile? = null
    set(value) {
        field = value
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return when (viewType) {
            R.layout.item_profile_resume -> HomeHolder.ProfileHolder(parent.inflate(viewType))
            else -> HomeHolder.MediaHolder(parent.inflate(viewType), actions)
        }
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        when (holder) {
            is HomeHolder.MediaHolder -> {
                holder.binding.apply {
                    media = getItem(position)
                }
            }
            is HomeHolder.ProfileHolder -> {
                holder.binding.profile = profile
            }
        }
    }

    override fun getItem(position: Int): InstagramMedia? {
        return if (position == 0) {
            profile?.toIgMedia()
        } else {
            super.getItem(position - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.item_profile_resume else R.layout.item_profile_home_media
    }

    sealed class HomeHolder(val view: View) : RecyclerView.ViewHolder(view) {
        class MediaHolder(
            val binding: ItemProfileHomeMediaBinding,
            actions: ProfileActions
        ) : HomeHolder(binding.root) {
            init {
                binding.actions = actions
            }
        }

        class ProfileHolder(val binding: ItemProfileResumeBinding) : HomeHolder(binding.root)
    }

    private object DiffCallback : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(
            oldItem: InstagramMedia,
            newItem: InstagramMedia
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: InstagramMedia,
            newItem: InstagramMedia
        ): Boolean {
            return oldItem == newItem
//            return when {
//                oldItem.isProfile && newItem.isProfile -> oldItem.wrappedProfile == newItem.wrappedProfile
//                !oldItem.isProfile && !newItem.isProfile -> oldItem.wrappedMedia == newItem.wrappedMedia
//                else -> true
//            }
        }
    }
}
