package com.cool.element.foobar.domain.entity.network
import com.google.gson.annotations.SerializedName

data class CarNetworkResponse(
    val title: String,
    val version: String,
    @SerializedName("results") val cars: List<CarNetwork>
)
