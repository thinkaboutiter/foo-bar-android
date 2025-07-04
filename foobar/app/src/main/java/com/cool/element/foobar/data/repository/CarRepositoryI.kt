package com.cool.element.foobar.data.repository
import com.cool.element.foobar.domain.entity.application.CarApp

interface CarRepositoryI {
    @Throws(Exception::class)
    suspend fun getNetworkCars(): List<CarApp>

    @Throws(Exception::class)
    suspend fun getLocalCars(): List<CarApp>
}