package com.cool.element.foobar.domain.entity.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CarNetwork(
    val title: String,
    @SerialName("href") val aboutUrlString: String,
    @SerialName("description") val details: String,
    @SerialName("thumbnail") val imageUrlString: String,
)
