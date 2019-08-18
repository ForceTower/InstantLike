package com.forcetower.likesview.core.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.forcetower.likesview.core.PagingRequestHelper
import com.forcetower.likesview.core.database.LikeDatabase
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.service.InstagramAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor

class ProfilePicturesBoundaryCallback(
    private val userId: Long,
    private val database: LikeDatabase,
    private val service: InstagramAPI,
    private val responseHandler: (Long, List<InstagramMedia>) -> Unit,
    private val executor: Executor,
    private val desiredWidth: Int
) : PagedList.BoundaryCallback<InstagramMedia>() {
    var loadingZero = false
    var loadingAfter = false
    // private val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        if (loadingZero) return
        loadingZero = true
        suspend {
            withContext(Dispatchers.IO) {
                val page = service.getProfilePage(userId)
                val profile = InstagramProfile.createFromFetch(page)
                val medias = InstagramMedia.getMediaListFromProfileFetch(page, desiredWidth, profile?.nextCachedPage)
                database.instagramMedia().insert(medias)
                loadingZero = false
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: InstagramMedia) {
        super.onItemAtEndLoaded(itemAtEnd)
        if (loadingAfter) return
        loadingAfter = true
        val nextPage = itemAtEnd.nextPage ?: return
        suspend {
            withContext(Dispatchers.IO) {
                val page = service.getProfilePage(userId, maxId = nextPage)
                val profile = InstagramProfile.createFromFetch(page)
                val medias = InstagramMedia.getMediaListFromProfileFetch(page, desiredWidth, profile?.nextCachedPage)
                database.instagramMedia().insert(medias)
                loadingAfter = false
            }
        }
    }
}