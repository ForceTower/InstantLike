package com.forcetower.likesview.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.forcetower.likesview.databinding.FragmentAddFirstProfileBinding

class AddProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddFirstProfileBinding.inflate(inflater, container, false).root
    }
}