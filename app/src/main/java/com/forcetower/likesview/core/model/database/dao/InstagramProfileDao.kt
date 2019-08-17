package com.forcetower.likesview.core.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.forcetower.likesview.core.model.database.InstagramProfile
import java.util.Calendar

@Dao
abstract class InstagramProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(value: InstagramProfile)

    @Query("SELECT * FROM InstagramProfile WHERE selected = 1")
    abstract fun getSelectedProfile(): LiveData<InstagramProfile>

    @Query("SELECT * FROM InstagramProfile WHERE username = :username")
    abstract fun getProfile(username: String): LiveData<InstagramProfile>

    @Query("SELECT username FROM InstagramProfile")
    abstract fun getUsernames(): LiveData<List<String>>

    @Query("DELETE FROM InstagramProfile WHERE username = :username")
    abstract suspend fun delete(username: String)

    @Query("SELECT username FROM InstagramProfile WHERE selected = 1")
    abstract suspend fun getSelectedUsernameDirect(): String?

    @Query("UPDATE InstagramProfile SET selected = 1, lastChecked = :lastChecked WHERE username = :username")
    protected abstract suspend fun markSelected(username: String, lastChecked: Long = Calendar.getInstance().timeInMillis)

    @Query("UPDATE InstagramProfile SET selected = 0")
    protected abstract suspend fun markUnselectedAll()

    @Transaction
    open suspend fun setCurrentProfile(username: String) {
        markUnselectedAll()
        markSelected(username)
    }
}