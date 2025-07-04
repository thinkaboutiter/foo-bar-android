package com.cool.element.foobar

import android.app.Application
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.domain.entity.local.CarLocal
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class FoobarApp : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DatabaseEntryPoint {
        fun database(): AppRoomDatabaseA
    }

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            createDatabaseRecords()
        }
    }

    private suspend fun createDatabaseRecords() {
        val entryPoint = EntryPointAccessors.fromApplication(this, DatabaseEntryPoint::class.java)
        val roomDatabase = entryPoint.database()
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