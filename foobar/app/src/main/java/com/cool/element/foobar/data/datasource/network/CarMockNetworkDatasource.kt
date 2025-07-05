package com.cool.element.foobar.data.datasource.network

import android.content.Context
import com.cool.element.foobar.R
import com.cool.element.foobar.data.parser.CarNetworkJsonParserI
import com.cool.element.foobar.domain.entity.network.CarNetwork
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CarMockNetworkDatasource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val parser: CarNetworkJsonParserI
): CarNetworkDatasourceI {
    @Throws(Exception::class)
    override suspend fun fetchCars(): List<CarNetwork> {
        val result = parser.parseCarModelsFromRaw(context, R.raw.mockdata)
        return result
    }
}