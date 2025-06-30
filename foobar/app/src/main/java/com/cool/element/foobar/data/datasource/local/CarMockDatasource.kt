package com.cool.element.foobar.data.datasource.local

import android.content.Context
import com.cool.element.foobar.R
import com.cool.element.foobar.data.datasource.CarDatasourceI
import com.cool.element.foobar.data.parser.CarNetworkJsonParser
import com.cool.element.foobar.data.parser.CarNetworkJsonParserI
import com.cool.element.foobar.domain.entity.network.CarNetwork

class CarMockDatasource(
    private val context: Context,
    private val parser: CarNetworkJsonParserI = CarNetworkJsonParser()
): CarDatasourceI {
    @Throws(Exception::class)
    override suspend fun fetchCars(): List<CarNetwork> {
        val result = parser.parseCarModelsFromRaw(context, R.raw.mockdata)
        return result
    }
}