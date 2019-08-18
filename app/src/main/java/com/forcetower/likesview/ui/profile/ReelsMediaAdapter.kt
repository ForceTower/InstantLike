package com.forcetower.likesview.ui.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.helpers.BasicProfile
import com.forcetower.likesview.databinding.ItemProfileReelBinding

class ReelsMediaAdapter(
    private val actions: ProfileActions
) : ListAdapter<BasicProfile, ReelsMediaAdapter.ReelHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelHolder {
        return ReelHolder(parent.inflate(R.layout.item_profile_reel), actions)
    }

    override fun onBindViewHolder(holder: ReelHolder, position: Int) {
        holder.binding.apply {
            profile = getItem(position)
        }
    }

    inner class ReelHolder(
        val binding: ItemProfileReelBinding,
        actions: ProfileActions
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actions = actions
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<BasicProfile>() {
        override fun areItemsTheSame(oldItem: BasicProfile, newItem: BasicProfile): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: BasicProfile, newItem: BasicProfile): Boolean {
            return oldItem == newItem
        }
    }
}
