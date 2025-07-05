package com.cool.element.foobar.data.datasource.local

import com.cool.element.foobar.domain.entity.local.CarLocal

interface CarLocalDatasourceI {
    @Throws(Exception::class)
    suspend fun getCarById(id: Long): CarLocal?

    @Throws(Exception::class)
    suspend fun getAllCars(): List<CarLocal>

    @Throws(Exception::class)
    suspend fun insertCar(car: CarLocal)

    @Throws(Exception::class)
    suspend fun updateCar(car: CarLocal)

    @Throws(Exception::class)
    suspend fun deleteCar(car: CarLocal)

    @Throws(Exception::class)
    suspend fun deleteCars(vararg cars: CarLocal)
}
