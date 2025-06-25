package com.cool.element.foobar.data.repository
import com.cool.element.foobar.domain.entity.application.CarApp

interface CarRepository {
    suspend fun getCars(): List<CarApp>
}