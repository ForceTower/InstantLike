package com.forcetower.likesview.ui.media

import androidx.lifecycle.ViewModel
import com.forcetower.likesview.core.repository.InstagramProfileRepository
import javax.inject.Inject

class MediaListViewModel @Inject constructor(
    private val repository: InstagramProfileRepository
) : ViewModel() {
    fun getSelectedProfileMediaSource(pageSize: Int) = repository.getSelectedProfileMediaSource(pageSize)
    fun getSelectedProfile() = repository.getSelectedProfile()
}