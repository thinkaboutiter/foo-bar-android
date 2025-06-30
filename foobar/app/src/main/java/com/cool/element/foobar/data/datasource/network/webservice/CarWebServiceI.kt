package com.cool.element.foobar.data.datasource.network.webservice
import com.cool.element.foobar.domain.entity.network.CarNetworkResponse
import retrofit2.Response
import retrofit2.http.*

interface CarWebServiceI {
    @GET("Disconnecter/ba9872ace953e382b3497ba358940ca9/raw/90f9f3344b0539a71e7abcb69578c6dadb817a86/gistfile1.txt/")
    suspend fun getCarNetworkResponse(): Response<CarNetworkResponse>

//    @GET("cars/{id}")
//    suspend fun getCarById(@Path("id") id: String): Response<CarNetwork>
//
//    @POST("cars")
//    suspend fun createCar(@Body car: CarRequest): Response<CarNetwork>
//
//    @PUT("cars/{id}")
//    suspend fun updateCar(@Path("id") id: String, @Body car: CarRequest): Response<CarNetwork>
//
//    @DELETE("cars/{id}")
//    suspend fun deleteCar(@Path("id") id: String): Response<Unit>
}