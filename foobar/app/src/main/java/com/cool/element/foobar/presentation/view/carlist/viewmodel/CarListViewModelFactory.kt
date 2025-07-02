package com.cool.element.foobar.presentation.view.carlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cool.element.foobar.data.repository.CarRepositoryI

class CarListViewModelFactory(
    private val repository: CarRepositoryI
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}