package com.cool.element.foobar.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cool.element.foobar.domain.entity.local.CarDao
import com.cool.element.foobar.domain.entity.local.CarLocal

@Database(
    entities = [CarLocal::class],
    version = 1
)
abstract class AppRoomDatabaseA : RoomDatabase() {

    abstract fun carDao(): CarDao

    companion object {
        const val DATABASE_NAME = "app_room_database"
    }
}