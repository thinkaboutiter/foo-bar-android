package com.cool.element.foobar.presentation.view.localcars

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.presentation.view.carlist.CarListView
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModel
import com.cool.element.foobar.utils.Constants

@Composable
fun LocalCarsView(
    modifier: Modifier = Modifier,
    viewModel: CarListViewModel
) {
    val message = "LocalCarsView created"
    Log.i(Constants.UI.LOG_TAG_UI, message)

    CarListView(
        modifier = modifier,
        strategy = RepositoryStrategy.LOCAL,
        viewModel = viewModel
    )
}