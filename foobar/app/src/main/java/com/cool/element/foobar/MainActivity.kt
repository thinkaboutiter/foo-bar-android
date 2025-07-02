package com.cool.element.foobar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.cool.element.foobar.data.datasource.local.CarLocalDatasource
import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.data.datasource.network.CarNetworkDataSource
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.data.repository.CarRepository
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.presentation.theme.FoobarTheme
import com.cool.element.foobar.presentation.view.bottomtabs.BottomTabsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoobarTheme {
                BottomTabsView(
                    modifier = Modifier.fillMaxSize(),
                    makeRepository = { context ->
                        val networkDatasource: CarNetworkDatasourceI = CarNetworkDataSource()
                        val app = context.applicationContext as FoobarApp
                        val carDao = app.roomDatabase.carDao()
                        val localDatasource: CarLocalDatasourceI = CarLocalDatasource(carDao = carDao)
                        val repository: CarRepositoryI = CarRepository(
                            networkDatasource,
                            localDatasource = localDatasource,
                        )
                        repository
                    }
                )
            }
        }
    }
}
