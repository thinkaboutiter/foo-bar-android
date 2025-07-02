package com.cool.element.foobar

import android.app.Application
import androidx.room.Room
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.domain.entity.local.CarLocal
import kotlinx.coroutines.runBlocking

class FoobarApp : Application() {

    val roomDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppRoomDatabaseA::class.java,
            AppRoomDatabaseA.DATABASE_NAME
        ).build()
    }

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            createDatabaseRecords()
        }
    }

    private suspend fun createDatabaseRecords() {
        val dao = roomDatabase.carDao()
        for (i in 1..10) {
            // check for car existence
            val existingCar = dao.getCarById(i.toLong())
            if (existingCar != null) {
                continue // Skip if the car already exists
            }

            val car = CarLocal(
                i.toLong(),
                "DB Car $i",
                "http",
                "${2020 + i}",
                "${4000 + 1}"
            )
            dao.insertCar(car)
        }
    }
}