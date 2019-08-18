package com.forcetower.likesview.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.forcetower.likesview.core.model.helpers.BasicProfile
import com.forcetower.likesview.core.model.values.InstagramProfile
import java.util.Calendar

@Dao
abstract class InstagramProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(value: InstagramProfile)

    @Query("SELECT * FROM InstagramProfile WHERE selected = 1")
    abstract fun getSelectedProfile(): LiveData<InstagramProfile?>

    @Query("SELECT * FROM InstagramProfile WHERE username = :username")
    abstract fun getProfile(username: String): LiveData<InstagramProfile>

    @Query("SELECT username, pictureUrl, selected, name FROM InstagramProfile")
    abstract fun getUsernames(): LiveData<List<BasicProfile>>

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

    @Transaction
    open suspend fun insertSelecting(profile: InstagramProfile) {
        markUnselectedAll()
        insert(profile)
        markSelected(profile.username)
    }

    @Query("SELECT * FROM InstagramProfile WHERE username = :username")
    abstract suspend fun getProfileDirect(username: String): InstagramProfile?

    @Query("SELECT * FROM InstagramProfile WHERE id = :userId")
    abstract suspend fun getProfileById(userId: Long): InstagramProfile?

    @Query("SELECT * FROM InstagramProfile WHERE selected = 1")
    abstract fun getSelectedProfileDirect(): InstagramProfile?

    @Query("UPDATE InstagramProfile SET nextCachedPage = :nextPage, hasCachedNextPage = :hasNextPage WHERE id = :id")
    abstract fun updateNextPage(id: Long, nextPage: String?, hasNextPage: Boolean)
}