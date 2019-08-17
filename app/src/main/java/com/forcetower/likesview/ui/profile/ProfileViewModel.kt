package com.forcetower.likesview.ui.profile

import androidx.lifecycle.*
import com.forcetower.likesview.core.extensions.limit
import com.forcetower.likesview.core.model.InstagramUserSearch
import com.forcetower.likesview.core.service.InstagramAPI
import com.forcetower.likesview.core.Event
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val service: InstagramAPI
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
}