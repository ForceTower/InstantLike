package com.forcetower.likesview.core.service

import com.forcetower.likesview.core.model.transfer.ProfileFetchResult
import com.forcetower.likesview.core.model.transfer.TopSearchResult
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

    @GET("graphql/query/?query_hash={queryHash}&variables={\"id\":{userId}, \"first\":\"{amount}\", \"after\": {maxId} }")
    suspend fun getProfilePage(
        @Path("userId") userId: Long,
        @Path("queryHash") queryHash: String = "42323d64886122307be10013ad2dcc44",
        @Path("amount") amount: Int = 12,
        @Path("maxId") maxId: String? = null
    ): ProfileFetchResult
}