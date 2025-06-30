package com.cool.element.foobar.data.datasource.network.webservice

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CarWebClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private var httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val webService: CarWebServiceI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarWebServiceI::class.java)
    }
}