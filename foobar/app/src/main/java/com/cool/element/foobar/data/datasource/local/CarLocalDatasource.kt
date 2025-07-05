package com.cool.element.foobar.data.datasource.local

import com.cool.element.foobar.domain.entity.local.CarDao
import com.cool.element.foobar.domain.entity.local.CarLocal
class CarLocalDatasource constructor(
    private val carDao: CarDao
) : CarLocalDatasourceI {
    @Throws
    override suspend fun getCarById(id: Long): CarLocal? {
        return carDao.getCarById(id)
    }

    @Throws
    override suspend fun getAllCars(): List<CarLocal> {
        return carDao.getAllCars()
    }

    @Throws
    override suspend fun insertCar(car: CarLocal) {
        carDao.insertCar(car)
    }

    @Throws
    override suspend fun updateCar(car: CarLocal) {
        carDao.updateCar(car)
    }

    @Throws
    override suspend fun deleteCar(car: CarLocal) {
        carDao.deleteCar(car)
    }

    @Throws
    override suspend fun deleteCars(vararg cars: CarLocal) {
        carDao.deleteCars()
    }
}