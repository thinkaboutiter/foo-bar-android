package com.cool.element.foobar.data.datasource
import com.cool.element.foobar.domain.entity.network.CarNetwork

interface CarDatasource {
    suspend fun getCars(): List<CarNetwork>
}
