package com.forcetower.likesview.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.databinding.FragmentHomeProfileBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeProfileFragment : DaggerFragment() {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentHomeProfileBinding
    private val viewModel: ProfileViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentHomeProfileBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile("joaopauloforce").observe(this, Observer {
            binding.profile = it
        })
    }
}