package com.forcetower.likesview.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.forcetower.likesview.core.extensions.doOnApplyWindowInsets
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.databinding.FragmentHomeProfileBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeProfileFragment : DaggerFragment() {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentHomeProfileBinding
    private val viewModel: ProfileViewModel by viewModels { factory }
    private lateinit var mediaAdapter: ProfileMediaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mediaAdapter = ProfileMediaAdapter()
        return FragmentHomeProfileBinding.inflate(inflater, container, false).also {
            binding = it
            binding.recyclerMedias.adapter = mediaAdapter
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerMedias.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        val username = "netflix"
        viewModel.getProfile(username).observe(this, Observer {
            binding.profile = it
        })
        viewModel.getMedias(username).observe(this, Observer {
            if (it.isNotEmpty()) {
                mediaAdapter.submitList(it)
            }
        })
    }
}