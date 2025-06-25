package com.cool.element.foobar.domain.entity.network

import com.google.gson.annotations.SerializedName

data class CarNetwork(
    val title: String,
    @SerializedName("href") val aboutUrlString: String,
    @SerializedName("description") val details: String,
    @SerializedName("thumbnail") val imageUrlString: String,
)
