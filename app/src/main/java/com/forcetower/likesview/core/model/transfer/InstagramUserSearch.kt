package com.forcetower.likesview.core.model.transfer

import com.google.gson.annotations.SerializedName

data class InstagramUserSearch (
    val pk: String,
    val username: String,
    @SerializedName("full_name")
    val name: String,
    @SerializedName("profile_pic_url")
    val pictureUrl: String,
    @SerializedName("is_private")
    val private: Boolean,
    @SerializedName("is_verified")
    val verified: Boolean
)