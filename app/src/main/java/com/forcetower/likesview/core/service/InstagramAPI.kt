package com.forcetower.likesview.core.service

import com.forcetower.likesview.core.model.transfer.ProfileFetchResult
import com.forcetower.likesview.core.model.transfer.TopSearchResult
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

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

    @GET("graphql/query")
    fun getProfilePage(
        @QueryMap(encoded = false) queryMap: Map<String, String>,
        @Query("query_hash") queryHash: String = "42323d64886122307be10013ad2dcc44"
    ): Call<ProfileFetchResult>

}