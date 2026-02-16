package com.librarix.data.di

import android.content.Context
import androidx.room.Room
import com.librarix.data.local.LibrarixDatabase
import com.librarix.data.local.dao.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LibrarixDatabase {
        return Room.databaseBuilder(
            context,
            LibrarixDatabase::class.java,
            "librarix_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: LibrarixDatabase): BookDao {
        return database.bookDao()
    }
}