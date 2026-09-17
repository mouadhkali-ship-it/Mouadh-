package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adb_bookmarks")
data class AdbBookmarkEntity(
    @PrimaryKey
    val commandId: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)
