package com.cool.element.foobar

import android.app.Application
import androidx.room.Room
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.data.datasource.local.CarLocalDatasource
import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasource
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.data.datasource.network.webservice.CarWebServiceI
import com.cool.element.foobar.data.repository.CarRepository
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.domain.entity.local.CarLocal
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoobarApp : Application() {

    // Manual DI - create dependencies manually
    val gson: Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val webService: CarWebServiceI by lazy {
        retrofit.create(CarWebServiceI::class.java)
    }

    val database: AppRoomDatabaseA by lazy {
        Room.databaseBuilder(
            this,
            AppRoomDatabaseA::class.java,
            "foobar_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    val networkDatasource: CarNetworkDatasourceI by lazy {
        CarNetworkDatasource(webService)
    }

    val localDatasource: CarLocalDatasourceI by lazy {
        CarLocalDatasource(database.carDao())
    }

    val repository: CarRepositoryI by lazy {
        CarRepository(networkDatasource, localDatasource)
    }

    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }

    private fun initializeDatabase() {
        runBlocking {
            val dao = database.carDao()
            
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
}