package com.cool.element.foobar.data.datasource.network.webservice

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CarWebClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/Disconnecter/ba9872ace953e382b3497ba358940ca9/raw/90f9f3344b0539a71e7abcb69578c6dadb817a86/gistfile1.txt"

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