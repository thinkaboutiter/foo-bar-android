package com.cool.element.foobar.presentation.view.bottomtabs

import com.cool.element.foobar.R
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.cool.element.foobar.presentation.view.localcars.LocalCarsView
import com.cool.element.foobar.presentation.view.networkcars.NetworkCarsView
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModel
import com.cool.element.foobar.utils.Constants

@Composable
fun BottomTabsView(
    modifier: Modifier = Modifier,
    repository: CarRepositoryI
) {
    val viewModel = remember { CarListViewModel(repository) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        BottomTabItem(
            title = Constants.UI.TAB_LOCAL_CARS,
            icon = ImageVector.vectorResource(R.drawable.database_24)
        ),
        BottomTabItem(
            title = Constants.UI.TAB_NETWORK_CARS,
            icon = ImageVector.vectorResource(R.drawable.cell_tower_24)
        )
    )

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(text = tab.title)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTabIndex) {
            0 -> LocalCarsView(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
            1 -> NetworkCarsView(
                modifier = Modifier.padding(paddingValues),
                viewModel = viewModel
            )
        }
    }
}

private data class BottomTabItem(
    val title: String,
    val icon: ImageVector
)