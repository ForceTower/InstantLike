package com.forcetower.likesview.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.forcetower.likesview.core.database.dao.InstagramMediaDao
import com.forcetower.likesview.core.model.values.InstagramProfile
import com.forcetower.likesview.core.database.dao.InstagramProfileDao
import com.forcetower.likesview.core.model.values.InstagramMedia
import com.forcetower.likesview.core.model.values.InstagramMediaContentsConverter

@Database(entities = [
    InstagramProfile::class,
    InstagramMedia::class
], version = 1, exportSchema = true)
@TypeConverters(value = [
    InstagramMediaContentsConverter::class
])
abstract class LikeDatabase : RoomDatabase() {
    abstract fun instagramProfiles(): InstagramProfileDao
    abstract fun instagramMedia(): InstagramMediaDao
}