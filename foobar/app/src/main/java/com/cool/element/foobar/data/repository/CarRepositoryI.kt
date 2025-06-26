package com.cool.element.foobar.data.repository
import com.cool.element.foobar.domain.entity.application.CarApp

interface CarRepositoryI {
    @Throws(Exception::class)
    suspend fun fetchCars(): List<CarApp>
}