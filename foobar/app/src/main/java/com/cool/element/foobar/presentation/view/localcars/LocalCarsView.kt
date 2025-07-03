package com.cool.element.foobar.presentation.view.localcars

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.presentation.view.carlist.CarListView

@Composable
fun LocalCarsView(
    modifier: Modifier = Modifier,
    makeRepository: (Context) -> CarRepositoryI
) {
    val message = "LocalCarsView created"
    Log.i("UI", message)

    CarListView(
        modifier = modifier,
        makeRepository = makeRepository,
        strategy = RepositoryStrategy.LOCAL
    )
}