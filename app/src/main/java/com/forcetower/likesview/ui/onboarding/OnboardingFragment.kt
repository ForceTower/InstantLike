package com.forcetower.likesview.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.forcetower.likesview.databinding.FragmentOnboardingStartBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OnboardingFragment : DaggerFragment() {
    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOnboardingStartBinding.inflate(inflater, container, false).also {
            it.btnContinue.setOnClickListener {
                preferences.edit().putBoolean("completed_onboarding", true).apply()
                val direction = OnboardingFragmentDirections.actionOnboardingToFirstProfile()
                findNavController().navigate(direction)
            }
        }.root
    }
}