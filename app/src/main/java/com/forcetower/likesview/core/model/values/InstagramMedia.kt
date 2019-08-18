package com.forcetower.likesview.core.model.values

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.forcetower.likesview.core.model.transfer.MediaGraph
import com.forcetower.likesview.core.model.transfer.ProfileFetchResult
import java.util.Calendar
import kotlin.math.abs

@Entity(indices = [
    Index(value = ["shortCode"], unique = true),
    Index(value = ["profileId"])
], foreignKeys = [
    ForeignKey(entity = InstagramProfile::class, parentColumns = ["id"], childColumns = ["profileId"], onUpdate = CASCADE, onDelete = CASCADE)
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
    val preview: String?,
    val isVideo: Boolean,
    val accessibilityCaption: String?,
    val thumbnailSrc: String?,
    val dimensionWidth: Int,
    val dimensionHeight: Int,
    val lastUpdated: Long
) {
    @Ignore
    val pictureUrlSmall = thumbnailSrc ?: displayUrl
    @Ignore
    val isGallery = type == "GraphSidecar"

    companion object {
        fun getMediaListFromProfileFetch(fetchResult: ProfileFetchResult, desiredWidth: Int): List<InstagramMedia> {
            val profile = fetchResult.graph?.user?.id ?: 0
            val edges = fetchResult.graph?.user?.edgeMedia?.edges ?: emptyList()
            return edges.map { createFromMediaProfileNode(it.node, desiredWidth, profile) }
        }

        private fun createFromMediaProfileNode(node: MediaGraph, desiredWidth: Int = 0, profileId: Long): InstagramMedia {
            return InstagramMedia(
                node.id,
                node.owner?.id ?: profileId,
                node.type,
                node.shortCode,
                node.displayUrl,
                node.likedEdge?.count ?: 0,
                node.takenAt,
                node.captionEdge?.edges?.firstOrNull { it.node.text != null }?.node?.text,
                node.mediaPreview,
                node.video,
                node.accessibilityCaption,
                findBestThumbnailForDevice(node, desiredWidth),
                node.dimensions?.width ?: 1,
                node.dimensions?.height ?: 1,
                Calendar.getInstance().timeInMillis
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
}