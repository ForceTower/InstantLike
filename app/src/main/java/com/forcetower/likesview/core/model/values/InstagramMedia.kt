package com.forcetower.likesview.core.model.values

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.forcetower.likesview.core.model.transfer.MediaGraph
import com.forcetower.likesview.core.model.transfer.ProfileFetchResult
import com.forcetower.likesview.core.model.values.InstagramMedia.GalleryContent.Companion.createDefault
import com.google.gson.Gson
import timber.log.Timber
import java.util.Calendar
import kotlin.math.abs

@Entity(indices = [
    Index(value = ["shortCode"], unique = true),
    Index(value = ["profileId"])
], foreignKeys = [
    ForeignKey(entity = InstagramProfile::class, parentColumns = ["id"], childColumns = ["profileId"], onUpdate = NO_ACTION, onDelete = CASCADE)
])
data class InstagramMedia (
    @PrimaryKey
    val id: Long,
    val profileId: Long,
    val type: String,
    val shortCode: String,
    val displayUrl: String,
    val likes: Int,
    val takenAt: Long,
    val caption: String?,
//    val preview: String?,
    val isVideo: Boolean,
    val accessibilityCaption: String?,
    val thumbnailSrc: String?,
    val dimensionWidth: Int,
    val dimensionHeight: Int,
    val lastUpdated: Long,
    val nextPage: String?,
    val contents: GalleryContent
) {
    @Ignore
    val pictureUrlSmall = thumbnailSrc ?: displayUrl
    @Ignore
    val isGallery = type == "GraphSidecar"
    @Ignore
    var profile: InstagramProfile? = null

    companion object {
        fun getMediaListFromProfileFetch(fetchResult: ProfileFetchResult, desiredWidth: Int, nextPage: String?): List<InstagramMedia> {
            val user = fetchResult.graph?.user ?: fetchResult.data?.user
            val profile = user?.id ?: 0
            val edges = user?.edgeMedia?.edges ?: emptyList()
            return edges.map { createFromMediaProfileNode(it.node, desiredWidth, profile, nextPage) }
        }

        private fun createFromMediaProfileNode(node: MediaGraph, desiredWidth: Int = 0, profileId: Long, nextPage: String? = null): InstagramMedia {
            return InstagramMedia(
                node.id,
                node.owner?.id ?: profileId,
                node.type,
                node.shortCode,
                node.displayUrl,
                node.likedEdge?.count ?: 0,
                node.takenAt,
                node.captionEdge?.edges?.firstOrNull { it.node.text != null }?.node?.text,
//                node.mediaPreview,
                node.video,
                node.accessibilityCaption,
                findBestThumbnailForDevice(node, desiredWidth),
                node.dimensions?.width ?: 1,
                node.dimensions?.height ?: 1,
                Calendar.getInstance().timeInMillis,
                nextPage,
                createDefault(node.displayUrl)
            )
        }

        private fun findBestThumbnailForDevice(node: MediaGraph, desiredWidth: Int): String? {
            val resources = node.thumbnailResources ?: return null
            if (resources.isEmpty()) return null
            if (desiredWidth == 0) return resources[0].src

            var dist = Int.MAX_VALUE
            var selected = resources[0]
            if (resources.size > 1) {
                for (resource in resources.subList(1, resources.size)) {
                    val distance = abs(resource.width - desiredWidth)
                    if (distance < dist) {
                        dist = distance
                        selected = resource
                    }
                }
            }
            return selected.src
        }
    }

    data class GalleryContent(
        val images: List<String>
    ) {
        companion object {
            fun createDefault(url: String) = GalleryContent(listOf(url))
        }
    }
}

object InstagramMediaContentsConverter {
    @JvmStatic
    @TypeConverter
    fun galleryContentToString(value: InstagramMedia.GalleryContent): String {
        return Gson().toJson(value)
    }

    @JvmStatic
    @TypeConverter
    fun stringToGalleryContent(value: String): InstagramMedia.GalleryContent {
        return Gson().fromJson(value, InstagramMedia.GalleryContent::class.java)
    }
}