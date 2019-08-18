package com.forcetower.likesview.ui.addprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.forcetower.likesview.core.EventObserver
import com.forcetower.likesview.core.extensions.closeKeyboard
import com.forcetower.likesview.core.model.InstagramUserSearch
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.vm.ViewModelFactory
import com.forcetower.likesview.databinding.FragmentAddFirstProfileBinding
import dagger.android.support.DaggerFragment
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import javax.inject.Inject

class AddProfileFragment : DaggerFragment(), KeyboardVisibilityEventListener {
    @Inject
    lateinit var factory: ViewModelFactory
    private var keyboardUnregister: Unregistrar? = null
    private lateinit var binding: FragmentAddFirstProfileBinding
    private val viewModel: AddProfileViewModel by viewModels { factory }
    private lateinit var adapter: AddProfileAdapter
    private var selected: InstagramUserSearch? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        keyboardUnregister = KeyboardVisibilityEvent.registerEventListener(requireActivity(), this)
        adapter = AddProfileAdapter(viewModel)
        return FragmentAddFirstProfileBinding.inflate(inflater, container, false).also {
            binding = it
            binding.recyclerSearch.adapter = adapter
            binding.btnAddProfile.setOnClickListener {
                insertProfileAndLeave()
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etSearch.addTextChangedListener(
            beforeTextChanged = { _, _, _, _ ->
                binding.btnAddProfile.isEnabled = false
            },
            onTextChanged = { text, _, _, _ ->
                val username = text?.toString()?.trim() ?: ""
                if (!username.startsWith("@")) {
                    val fixed = "@$username"
                    binding.etSearch.setText(fixed)
                    binding.etSearch.setSelection(fixed.length)
                }
            },
            afterTextChanged = { text ->
                val username = (text?.toString() ?: "").replace("@", "")
                viewModel.searchUsers(username)
            }
        )

        viewModel.userSearch.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.onAddProfileClick.observe(this, EventObserver {
            selected = it
            val fixed = "@${it.username}"
            binding.etSearch.run {
                setText(fixed)
                setSelection(fixed.length)
                clearFocus()
                closeKeyboard()
            }
            binding.btnAddProfile.isEnabled = true
        })
    }

    private fun insertProfileAndLeave() {
        val current = selected
        if (current != null) {
            viewModel.insertProfile(InstagramProfile.createFromSearch(current))
            val directions = AddProfileFragmentDirections.actionFirstProfileToProfileActivity()
            findNavController().navigate(directions)
        }
    }

    override fun onVisibilityChanged(isOpen: Boolean) {
        if (isOpen) {
            hideLayout()
        } else {
            showLayout()
        }
    }

    private fun hideLayout() {
        binding.btnAddProfile.visibility = View.GONE
        binding.logoView.visibility = View.GONE
        binding.spaceTop.visibility = View.GONE
        binding.spaceBot.visibility = View.GONE
        binding.recyclerSearch.visibility = View.VISIBLE
    }

    private fun showLayout() {
        binding.btnAddProfile.visibility = View.VISIBLE
        binding.logoView.visibility = View.VISIBLE
        binding.spaceTop.visibility = View.VISIBLE
        binding.spaceBot.visibility = View.VISIBLE
        binding.recyclerSearch.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        keyboardUnregister?.unregister()
    }
}