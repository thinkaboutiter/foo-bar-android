package com.cool.element.foobar.presentation.view.carlist.viewmodel

import android.util.Log
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.domain.entity.application.CarApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.jvm.Throws

@HiltViewModel
class CarListViewModel @Inject constructor(
    private val repository: CarRepositoryI,
) : CarListViewModelA() {

    @Throws(Exception::class)
    override suspend fun getCars(strategy: RepositoryStrategy): List<CarApp> {
        val result = when (strategy) {
            RepositoryStrategy.LOCAL -> {
                getLocalCars()
            }
            RepositoryStrategy.NETWORK -> {
                getNetworkCars()
            }
        }
        return result
    }

    @Throws(Exception::class)
    private suspend fun getLocalCars(): List<CarApp> {
        val message = "CarListViewModel -> Getting local cars"
        Log.i("UI", message)

        val result = repository.getLocalCars()
        return result
    }

    @Throws(Exception::class)
    private suspend fun getNetworkCars(): List<CarApp> {
        val message = "CarListViewModel -> Getting network cars"
        Log.i("UI", message)
        val result = repository.getNetworkCars()
        return result
    }
}

