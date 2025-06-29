package com.cool.element.foobar.presentation.view.carlist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cool.element.foobar.data.datasource.MockCarDatasource
import com.cool.element.foobar.data.repository.CarRepository

class MockCarListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            val datasource = MockCarDatasource(context)
            val repository = CarRepository(datasource)
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}