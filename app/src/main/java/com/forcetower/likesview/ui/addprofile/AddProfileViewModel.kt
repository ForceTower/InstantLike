package com.forcetower.likesview.ui.addprofile

import androidx.lifecycle.*
import com.forcetower.likesview.core.extensions.limit
import com.forcetower.likesview.core.model.transfer.InstagramUserSearch
import com.forcetower.likesview.core.service.InstagramAPI
import com.forcetower.likesview.core.Event
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.repository.InstagramProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddProfileViewModel @Inject constructor(
    private val service: InstagramAPI,
    private val repository: InstagramProfileRepository
) : ViewModel(), ProfileActions {
    private var currentSource: LiveData<List<InstagramUserSearch>>? = null

    private val _userSearch = MediatorLiveData<List<InstagramUserSearch>>()
    val userSearch: LiveData<List<InstagramUserSearch>>
        get() = _userSearch

    private val _searchingUsers = MutableLiveData<Boolean>()
    val searchingUsers: LiveData<Boolean>
        get() = _searchingUsers

    private val _onAddProfileClick = MutableLiveData<Event<InstagramUserSearch>>()
    val onAddProfileClick: LiveData<Event<InstagramUserSearch>>
        get() = _onAddProfileClick

    fun searchUsers(username: String) {
        val data = liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            val res = service.topSearch(username).users?.limit(3)?.map { it.user } ?: emptyList()
            emit(res)
        }

        val src = currentSource
        if (src != null) {
            _userSearch.removeSource(src)
        }
        _searchingUsers.value = true
        currentSource = data
        _userSearch.addSource(data) {
            _userSearch.removeSource(data)
            _searchingUsers.value = false
            _userSearch.value = it
        }
    }

    override fun onAddProfileClick(user: InstagramUserSearch?) {
        user ?: return
        _onAddProfileClick.value = Event(user)
    }

    fun insertProfile(profile: InstagramProfile) {
        viewModelScope.launch {
            repository.insertProfile(profile)
        }
    }
}