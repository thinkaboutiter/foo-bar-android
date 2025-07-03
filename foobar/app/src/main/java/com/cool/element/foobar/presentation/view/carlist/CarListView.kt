package com.cool.element.foobar.presentation.view.carlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Context
import android.util.Log
import com.cool.element.foobar.domain.entity.application.CarApp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModel
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModelFactory

@Composable
fun CarListView(
    modifier: Modifier = Modifier,
    makeRepository: (Context) -> CarRepositoryI,
    strategy: RepositoryStrategy
) {

    val message = "CarListView strategy=$strategy"
    Log.i("UI", message)

    val repository: CarRepositoryI = makeRepository(LocalContext.current)
    val factory = remember { CarListViewModelFactory(repository) }
    val viewModel: CarListViewModel = viewModel(factory = factory)

    var cars by remember { mutableStateOf<List<CarApp>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch cars when the composable is first created
    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        try {
            cars = viewModel.getCars(strategy)
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    // UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {
                LazyColumn {
                    items(cars) { car ->
                        CarRowView(car = car)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarListViewPreview() {
    val sampleCars = listOf(
        CarApp(
            title = "Tesla Model S",
            aboutUrlString = "https://tesla.com/models",
            details = "Electric sedan with long range.",
            imageUrlString = ""
        ),
        CarApp(
            title = "BMW i4",
            aboutUrlString = "https://bmw.com/i4",
            details = "Sporty electric Gran Coupé.",
            imageUrlString = ""
        )
    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn {
            items(sampleCars) { car ->
                CarRowView(car = car)
            }
        }
    }
}
