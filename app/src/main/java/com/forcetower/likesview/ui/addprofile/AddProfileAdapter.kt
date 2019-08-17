package com.forcetower.likesview.ui.addprofile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.forcetower.likesview.R
import com.forcetower.likesview.core.extensions.inflate
import com.forcetower.likesview.core.model.InstagramUserSearch
import com.forcetower.likesview.databinding.ItemAddFirstProfileBinding

class AddProfileAdapter(
    private val actions: ProfileActions
) : ListAdapter<InstagramUserSearch, AddProfileAdapter.ProfileHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        return ProfileHolder(parent.inflate(R.layout.item_add_first_profile), actions)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.binding.apply {
            user = getItem(position)
            executePendingBindings()
        }
    }

    inner class ProfileHolder(
        val binding: ItemAddFirstProfileBinding,
        actions: ProfileActions
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.actions = actions
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<InstagramUserSearch>() {
        override fun areItemsTheSame(
            oldItem: InstagramUserSearch,
            newItem: InstagramUserSearch
        ) = oldItem.pk == newItem.pk

        override fun areContentsTheSame(
            oldItem: InstagramUserSearch,
            newItem: InstagramUserSearch
        ) = oldItem == newItem
    }
}