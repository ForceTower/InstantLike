package com.forcetower.likesview.ui.addprofile

import com.forcetower.likesview.core.model.transfer.InstagramUserSearch

interface ProfileActions {
    fun onAddProfileClick(user: InstagramUserSearch?)
}