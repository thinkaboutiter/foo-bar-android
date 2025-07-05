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
import com.cool.element.foobar.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class FoobarApp : Application() {

    // Manual DI - create dependencies manually
    private val json: Json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.Network.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(Constants.Network.CONTENT_TYPE_JSON.toMediaType()))
            .build()
    }

    private val webService: CarWebServiceI by lazy {
        retrofit.create(CarWebServiceI::class.java)
    }

    private val database: AppRoomDatabaseA by lazy {
        Room.databaseBuilder(
            this,
            AppRoomDatabaseA::class.java,
            Constants.Database.NAME
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    private val networkDatasource: CarNetworkDatasourceI by lazy {
        CarNetworkDatasource(webService)
    }

    private val localDatasource: CarLocalDatasourceI by lazy {
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
            
            for (i in 1..Constants.MockData.MOCK_CARS_COUNT) {
                // check for car existence
                val existingCar = dao.getCarById(i.toLong())
                if (existingCar != null) {
                    continue // Skip if the car already exists
                }

                val car = CarLocal(
                    i.toLong(),
                    "${Constants.MockData.CAR_TITLE_PREFIX} $i",
                    Constants.MockData.CAR_URL_PREFIX,
                    "${Constants.MockData.CAR_YEAR_BASE + i}",
                    "${Constants.MockData.CAR_PRICE_BASE + 1}"
                )
                dao.insertCar(car)
            }
        }
    }
}
