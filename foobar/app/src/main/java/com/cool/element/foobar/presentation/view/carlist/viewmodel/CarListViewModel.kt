package com.cool.element.foobar.presentation.view.carlist.viewmodel

import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.domain.entity.application.CarApp
import kotlin.jvm.Throws

class CarListViewModel(
    val repository: CarRepositoryI
) : CarListViewModelA() {

    @Throws(Exception::class)
    override suspend fun fetchCars(): List<CarApp> {
        val result = repository.fetchCars()
        return result
    }
}

