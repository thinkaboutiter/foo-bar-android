package com.cool.element.foobar.data.datasource

import com.cool.element.foobar.domain.entity.network.CarNetwork

interface CarDatasourceI {
    @Throws(Exception::class)
    suspend fun fetchCars(): List<CarNetwork>
}
