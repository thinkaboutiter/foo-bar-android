package com.cool.element.foobar.data.repository

import android.content.Context
import com.cool.element.foobar.data.datasource.CarDatasourceI
import com.cool.element.foobar.domain.entity.application.CarApp
import kotlin.jvm.Throws

class CarRepository(
    private val datasourceI: CarDatasourceI
): CarRepositoryI {

    @Throws(Exception::class)
    override suspend fun getCars(): List<CarApp> {
        val result = datasourceI.fetchCars().map { carNetwork ->
            CarApp.from(carNetwork)
        }
        return result
    }
}