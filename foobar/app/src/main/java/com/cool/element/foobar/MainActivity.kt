package com.cool.element.foobar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.cool.element.foobar.presentation.theme.FoobarTheme
import com.cool.element.foobar.presentation.view.bottomtabs.BottomTabsView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoobarTheme {
                BottomTabsView(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
