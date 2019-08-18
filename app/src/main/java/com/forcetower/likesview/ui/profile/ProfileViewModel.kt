package com.forcetower.likesview.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.repository.InstagramProfileRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: InstagramProfileRepository
) : ViewModel() {
    fun getProfile(username: String): LiveData<InstagramProfile> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(repository.getProfile(username))
        }
    }

    fun getMedias(username: String): LiveData<List<InstagramMedia>> {
        return repository.getMedias(username)
    }

    fun getSelectedProfile() = repository.getSelectedProfile()
}