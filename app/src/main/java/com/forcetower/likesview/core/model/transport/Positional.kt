package com.forcetower.likesview.core.model.transport

import com.forcetower.likesview.core.model.InstagramUserSearch

data class PositionalUser(
    val position: Int,
    val user: InstagramUserSearch
)