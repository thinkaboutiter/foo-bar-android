package com.cool.element.foobar.di

import com.cool.element.foobar.data.datasource.local.CarLocalDatasource
import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasource
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindCarNetworkDatasource(
        carNetworkDatasource: CarNetworkDatasource
    ): CarNetworkDatasourceI

    @Binds
    @Singleton
    abstract fun bindCarLocalDatasource(
        carLocalDatasource: CarLocalDatasource
    ): CarLocalDatasourceI
}