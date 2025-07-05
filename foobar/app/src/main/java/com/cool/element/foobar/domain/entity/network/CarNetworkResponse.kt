package com.cool.element.foobar.domain.entity.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarNetworkResponse(
    val title: String,
    val version: String,
    @SerialName("results") val cars: List<CarNetwork>
)
