package com.forcetower.likesview.ui

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.forcetower.likesview.core.Event
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
    preferences: SharedPreferences
) : ViewModel() {
    private val _launchDestination = MediatorLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>>
        get() = _launchDestination

    init {
        val onboarding = preferences.getBoolean("completed_onboarding", false)
        val profiles = preferences.getInt("profiles_added", 0)
        if (!onboarding) {
            _launchDestination.value = Event(LaunchDestination.ONBOARDING)
        } else if (profiles == 0) {
            _launchDestination.value = Event(LaunchDestination.ONBOARDING_NO_PROFILES)
        } else {
            _launchDestination.value = Event(LaunchDestination.MAIN_ACTIVITY)
        }
    }
}

enum class LaunchDestination {
    ONBOARDING,
    ONBOARDING_NO_PROFILES,
    MAIN_ACTIVITY
}