package com.cool.element.foobar

import android.app.Application
import androidx.room.Room
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA

class FoobarApp: Application() {

    val roomDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppRoomDatabaseA::class.java,
            AppRoomDatabaseA.DATABASE_NAME
        ).build()
    }
}