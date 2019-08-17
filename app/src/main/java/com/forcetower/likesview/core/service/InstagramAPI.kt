package com.forcetower.likesview.core.service

import com.forcetower.likesview.core.model.transport.ProfileFetchResult
import com.forcetower.likesview.core.model.transport.TopSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InstagramAPI {
    @GET("web/search/topsearch")
    suspend fun topSearch(
        @Query("query") query: String,
        @Query("context") context: String = "blended",
        @Query("rank_token") rankToken: Double = 0.9326825834917027,
        @Query("include_reel") includeReel: Boolean = true
    ): TopSearchResult

    @GET("{username}?__a=1")
    suspend fun getUser(
        @Path("username") username: String
    ): ProfileFetchResult
}