package com.cool.element.foobar.data.repository

import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.domain.entity.application.CarApp
import kotlin.jvm.Throws

class CarRepository(
    private val networkDatasource: CarNetworkDatasourceI,
    private val localDatasource: CarLocalDatasourceI
): CarRepositoryI {

    @Throws(Exception::class)
    override suspend fun getNetworkCars(): List<CarApp> {
        val result = networkDatasource.fetchCars().map { carNetwork ->
            CarApp.fromCarNetwork(carNetwork)
        }
        return result
    }

    @Throws(Exception::class)
    override suspend fun getLocalCars(): List<CarApp> {
        val result = localDatasource.getAllCars().map { carLocal ->
            CarApp.fromCarLocal(carLocal)
        }
        return result
    }
}