package com.forcetower.likesview.ui.media

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.databinding.FragmentMediaDetailsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


class MediaListFragment : DaggerFragment() {
    private var scrolled: Boolean = false
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: FragmentMediaDetailsBinding
    private lateinit var mediasAdapter: MediaDetailsAdapter
    private val args: MediaListFragmentArgs by navArgs()
    private val viewModel: MediaListViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mediasAdapter = MediaDetailsAdapter()
        return FragmentMediaDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.recyclerMedias.run {
                adapter = mediasAdapter
            }
            binding.recyclerMedias.visibility = View.INVISIBLE
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrolled = savedInstanceState?.getBoolean("scrolled_instance", false) ?: false

        viewModel.getSelectedProfile().observe(this, Observer {
            binding.profile = it
        })
        viewModel.getSelectedProfileMediaSource(6).observe(this, Observer {
            mediasAdapter.submitList(it)
            if (!scrolled) {
                scrolled = true
                Timber.d("Position to scroll ${args.position}")
                val manager = binding.recyclerMedias.layoutManager as LinearLayoutManager
                val smoothScroller = object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                smoothScroller.targetPosition = args.position
                manager.startSmoothScroll(smoothScroller)
                Handler(Looper.getMainLooper()).postDelayed({
                    manager.scrollToPosition(args.position)
                    binding.recyclerMedias.visibility = View.VISIBLE
                }, 100)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("scrolled_instance", scrolled)
    }
}