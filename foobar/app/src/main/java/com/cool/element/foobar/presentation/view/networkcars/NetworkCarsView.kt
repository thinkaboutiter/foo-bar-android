package com.cool.element.foobar.presentation.view.networkcars

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.presentation.view.carlist.CarListView
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModel

@Composable
fun NetworkCarsView(
    modifier: Modifier = Modifier,
    viewModel: CarListViewModel
) {
    val message = "NetworkCarsView created"
    Log.i("UI", message)

    CarListView(
        modifier = modifier,
        strategy = RepositoryStrategy.NETWORK,
        viewModel = viewModel
    )
}