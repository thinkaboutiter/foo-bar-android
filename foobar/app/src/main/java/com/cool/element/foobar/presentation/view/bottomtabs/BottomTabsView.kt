package com.cool.element.foobar.presentation.view.bottomtabs

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Storage
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
import com.cool.element.foobar.presentation.view.localcars.LocalCarsView
import com.cool.element.foobar.presentation.view.networkcars.NetworkCarsView

@Composable
fun BottomTabsView(
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        BottomTabItem(
            title = "Local Cars",
            icon = Icons.Default.Storage
        ),
        BottomTabItem(
            title = "Network Cars",
            icon = Icons.Default.Cloud
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
                modifier = Modifier.padding(paddingValues)
            )
            1 -> NetworkCarsView(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

private data class BottomTabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)