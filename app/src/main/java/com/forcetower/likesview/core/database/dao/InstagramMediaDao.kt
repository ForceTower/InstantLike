package com.forcetower.likesview.core.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.forcetower.likesview.core.model.values.InstagramMedia

@Dao
abstract class InstagramMediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(value: InstagramMedia)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(values: List<InstagramMedia>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNonBlock(values: List<InstagramMedia>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(value: InstagramMedia)

    @Query("SELECT im.* FROM InstagramMedia im INNER JOIN InstagramProfile ip ON im.profileId = ip.id WHERE ip.username = :username ORDER BY im.takenAt DESC")
    abstract fun getMediasOfProfile(username: String): LiveData<List<InstagramMedia>>

    @Query("SELECT im.* FROM InstagramMedia im INNER JOIN InstagramProfile ip ON im.profileId = ip.id WHERE ip.selected = 1 ORDER BY im.takenAt DESC")
    abstract fun getMediasOfSelectedProfile(): LiveData<List<InstagramMedia>>

    @Query("SELECT im.* FROM InstagramMedia im INNER JOIN InstagramProfile ip ON im.profileId = ip.id WHERE ip.selected = 1 ORDER BY im.takenAt DESC")
    abstract fun getMediasSourceOfSelectedProfile(): DataSource.Factory<Int, InstagramMedia>
}
