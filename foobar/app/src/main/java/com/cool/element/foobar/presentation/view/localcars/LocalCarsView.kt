package com.cool.element.foobar.presentation.view.localcars

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cool.element.foobar.presentation.view.carlist.CarListView

@Composable
fun LocalCarsView(
    modifier: Modifier = Modifier
) {
    CarListView(
        modifier = modifier,
        context = LocalContext.current
    )
}