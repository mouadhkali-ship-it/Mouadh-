package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdbBookmarkDao {
    @Query("SELECT commandId FROM adb_bookmarks ORDER BY savedAtTimestamp DESC")
    fun getAllBookmarkedIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: AdbBookmarkEntity)

    @Query("DELETE FROM adb_bookmarks WHERE commandId = :commandId")
    suspend fun deleteBookmark(commandId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM adb_bookmarks WHERE commandId = :commandId)")
    suspend fun isBookmarked(commandId: String): Boolean
}
