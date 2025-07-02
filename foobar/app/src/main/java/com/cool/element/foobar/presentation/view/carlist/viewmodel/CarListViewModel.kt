package com.cool.element.foobar.presentation.view.carlist.viewmodel

import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.domain.entity.application.CarApp
import kotlin.jvm.Throws

class CarListViewModel(
    val repository: CarRepositoryI,
    val repositoryStrategy: RepositoryStrategy
) : CarListViewModelA() {

    @Throws(Exception::class)
    override suspend fun getCars(): List<CarApp> {
        val result = when (repositoryStrategy) {
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
        val result = repository.getLocalCars()
        return result
    }

    @Throws(Exception::class)
    private suspend fun getNetworkCars(): List<CarApp> {
        val result = repository.getNetworkCars()
        return result
    }
}

