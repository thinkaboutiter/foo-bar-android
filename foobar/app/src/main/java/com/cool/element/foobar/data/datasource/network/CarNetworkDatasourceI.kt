package com.cool.element.foobar.data.datasource.network

import com.cool.element.foobar.domain.entity.network.CarNetwork

interface CarNetworkDatasourceI {
    @Throws(Exception::class)
    suspend fun fetchCars(): List<CarNetwork>
}