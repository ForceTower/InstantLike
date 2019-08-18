package com.forcetower.likesview.ui.profile

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.forcetower.likesview.R
import com.forcetower.likesview.core.DistinctObserver
import com.forcetower.likesview.core.EventObserver
import com.forcetower.likesview.core.extensions.doOnApplyWindowInsets
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.databinding.FragmentHomeProfileBinding
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HomeProfileFragment : DaggerFragment() {
    private var currentList: PagedList<InstagramMedia>? = null
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentHomeProfileBinding
    private val viewModel: ProfileViewModel by viewModels { factory }
    private lateinit var mediaAdapter: ProfileMediaAdapter
    private lateinit var reelsAdapter: ReelsMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mediaAdapter = ProfileMediaAdapter(viewModel)
        reelsAdapter = ReelsMediaAdapter(viewModel)

        val columns = resources.getInteger(R.integer.home_profile_media_column)

        return FragmentHomeProfileBinding.inflate(inflater, container, false).also {
            binding = it
            binding.recyclerMedias.apply {
                adapter = mediaAdapter
                setHasFixedSize(true)
                (layoutManager as GridLayoutManager).run {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (position == 0) columns else 1
                        }
                    }
                }
            }
            binding.recyclerReels.adapter = reelsAdapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerMedias.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        viewModel.getSelectedProfile().observe(this, Observer {
            Timber.d("Profile ${it?.username}")
            binding.profile = it
            mediaAdapter.profile = it
        })
        viewModel.getSelectedProfileMediaSource().observe(this, Observer { list ->
            Timber.d("Sized ${list.size}")
            mediaAdapter.submitList(list)
            currentList = list
        })
        viewModel.getProfiles().observe(this, Observer {
            Timber.d("Reels ${it.size}")
            reelsAdapter.submitList(it)
        })
        viewModel.mediaClick.observe(this, EventObserver { media ->
            val index = currentList?.indexOfFirst { it.id == media.id } ?: 0
            val directions = HomeProfileFragmentDirections.actionHomeToMediaList(if (index >= 0) index else 0)
            findNavController().navigate(directions)
        })
    }
}