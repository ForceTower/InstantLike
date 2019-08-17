package com.forcetower.likesview.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.forcetower.likesview.core.model.database.InstagramProfile
import com.forcetower.likesview.core.model.database.dao.InstagramProfileDao

@Database(entities = [
    InstagramProfile::class
], version = 1, exportSchema = true)
abstract class LikeDatabase : RoomDatabase() {
    abstract fun instagramProfiles(): InstagramProfileDao
}