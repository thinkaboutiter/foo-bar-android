package com.cool.element.foobar.presentation.view.carlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cool.element.foobar.data.datasource.CarDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDataSource
import com.cool.element.foobar.data.repository.CarRepository
import com.cool.element.foobar.data.repository.CarRepositoryI

class CarListViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            val datasource: CarDatasourceI = CarNetworkDataSource()
            val repository: CarRepositoryI = CarRepository(datasource)
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}