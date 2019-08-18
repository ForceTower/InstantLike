package com.forcetower.likesview.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.forcetower.likesview.core.Event
import com.forcetower.likesview.core.model.helpers.BasicProfile
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.repository.InstagramProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: InstagramProfileRepository
) : ViewModel(), ProfileActions {
    fun getProfiles() = repository.getAvailableProfiles()

    fun getProfile(username: String): LiveData<InstagramProfile> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(repository.getProfile(username))
        }
    }

    fun getMedias(username: String): LiveData<List<InstagramMedia>> {
        return repository.getMedias(username)
    }

    fun getSelectedProfileMedias(): LiveData<List<InstagramMedia>> {
        return repository.getSelectedProfileMedias()
    }

    fun getSelectedProfile() = repository.getSelectedProfile()

    override fun onReelClicked(profile: BasicProfile?) {
        profile ?: return
        viewModelScope.launch {
            repository.setSelectedProfile(profile.username)
        }
    }

    override fun onMediaClicked(media: InstagramMedia?) {
        media ?: return
    }
}