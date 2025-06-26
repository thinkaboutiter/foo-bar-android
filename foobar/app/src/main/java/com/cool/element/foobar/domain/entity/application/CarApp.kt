package com.cool.element.foobar.domain.entity.application

import com.cool.element.foobar.domain.entity.network.CarNetwork

data class CarApp(
    val title: String,
    val aboutUrlString: String,
    val details: String,
    val imageUrlString: String,
) {
    companion object {
        fun from(carNetwork: CarNetwork) = CarApp(
            title = carNetwork.title,
            aboutUrlString = carNetwork.aboutUrlString,
            details = carNetwork.details,
            imageUrlString = carNetwork.imageUrlString
        )
    }
}
