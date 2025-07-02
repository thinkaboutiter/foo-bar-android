package com.cool.element.foobar.presentation.view.networkcars

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.presentation.view.carlist.CarListView

@Composable
fun NetworkCarsView(
    modifier: Modifier = Modifier,
    makeRepository: (Context) -> CarRepositoryI
) {
    CarListView(
        modifier = modifier,
        makeRepository = makeRepository,
    )
}