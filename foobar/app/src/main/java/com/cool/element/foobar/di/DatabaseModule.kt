package com.cool.element.foobar.di

import android.content.Context
import androidx.room.Room
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.domain.entity.local.CarDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppRoomDatabaseA {
        return Room.databaseBuilder(
            context,
            AppRoomDatabaseA::class.java,
            "foobar_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCarDao(database: AppRoomDatabaseA): CarDao {
        return database.carDao()
    }
}