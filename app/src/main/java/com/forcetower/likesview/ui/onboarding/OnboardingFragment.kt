package com.forcetower.likesview.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.forcetower.likesview.databinding.FragmentOnboardingStartBinding

class OnboardingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOnboardingStartBinding.inflate(inflater, container, false).also {
            it.btnContinue.setOnClickListener {
                val direction = OnboardingFragmentDirections.actionOnboardingToFirstProfile()
                findNavController().navigate(direction)
            }
        }.root
    }
}