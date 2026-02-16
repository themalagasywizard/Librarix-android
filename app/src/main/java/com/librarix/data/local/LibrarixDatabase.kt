package com.librarix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.librarix.data.local.dao.BookDao
import com.librarix.data.local.entity.BookEntity

@Database(
    entities = [BookEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LibrarixDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
