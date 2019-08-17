package com.forcetower.likesview.core.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.forcetower.likesview.core.model.transport.ProfileFetchResult
import timber.log.Timber
import java.util.Calendar

@Entity(indices = [
    Index(value = ["username"], unique = true)
])
data class InstagramProfile(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val username: String,
    val name: String?,
    val pictureUrl: String?,
    val pictureUrlHd: String?,
    val biography: String?,
    val followingCount: Int,
    val followersCount: Int,
    val postCount: Int,
    @ColumnInfo(name = "private")
    val isPrivate: Boolean,
    @ColumnInfo(name = "verified")
    val isVerified: Boolean,
    val nextCachedPage: String?,
    val hasCachedNextPage: Boolean,
    val lastUpdate: Long,
    @ColumnInfo(name = "selected")
    var isSelected: Boolean = false,
    var lastChecked: Long = 0,
    var insertedAt: Long = 0
) {
    companion object {
        fun createFromFetch(fetchResult: ProfileFetchResult): InstagramProfile? {
            Timber.d("Creating using fetch: $fetchResult")
            val user = fetchResult.graph?.user
            user ?: return null
            return InstagramProfile(
                user.id,
                user.username,
                user.name,
                user.pictureUrl,
                user.pictureUrlHd,
                user.biography,
                user.edgeFollow?.count ?: -1,
                user.edgeFollowed?.count ?: -1,
                user.edgeMedia?.count ?: -1,
                user.private,
                user.verified,
                user.edgeMedia?.pageInfo?.endCursor,
                user.edgeMedia?.pageInfo?.hasNextPage ?: false,
                Calendar.getInstance().timeInMillis
            )
        }
    }
}