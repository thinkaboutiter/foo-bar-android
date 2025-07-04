package com.cool.element.foobar.data.datasource.network

import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.data.datasource.network.webservice.CarWebClient
import com.cool.element.foobar.data.datasource.network.webservice.CarWebServiceI
import com.cool.element.foobar.domain.entity.network.CarNetwork

class CarNetworkDataSource(
    private val webService: CarWebServiceI = CarWebClient.webService
) : CarNetworkDatasourceI {

    @Throws(Exception::class)
    override suspend fun fetchCars(): List<CarNetwork> {
        val response = webService.getCarNetworkResponse()

        when {
            response.isSuccessful.not() -> {
                throw Exception("Network call failed with code: ${response.code()}")
            }
            response.isSuccessful && response.body() != null -> {
                return response.body()!!.cars
            }
            response.isSuccessful && response.body() == null -> {
                throw Exception("Response body is null")
            }
            else -> {
                throw Exception("Unexpected response: ${response.errorBody()?.string()}")
            }
        }
    }
}