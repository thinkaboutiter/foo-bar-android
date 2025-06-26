package com.cool.element.foobar.presentation.view.carlistview

import androidx.lifecycle.ViewModel
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.domain.entity.application.CarApp
import kotlin.jvm.Throws

interface CarListViewModelI {
    @Throws(Exception::class)
    suspend fun fetchCars(): List<CarApp>
}

abstract class CarListViewModelA: ViewModel(), CarListViewModelI


class CarListViewModel(
    val repository: CarRepositoryI
) : CarListViewModelA() {

    @Throws(Exception::class)
    override suspend fun fetchCars(): List<CarApp> {
        val result = repository.fetchCars()
        return result
    }
}

