package com.cool.element.foobar.presentation.view.carlist.viewmodel

import androidx.lifecycle.ViewModel
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.domain.entity.application.CarApp

interface CarListViewModelI {
    @Throws(Exception::class)
    suspend fun getCars(strategy: RepositoryStrategy): List<CarApp>
}

abstract class CarListViewModelA: ViewModel(), CarListViewModelI
