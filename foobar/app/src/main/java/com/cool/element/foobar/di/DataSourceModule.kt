package com.cool.element.foobar.di

import com.cool.element.foobar.data.datasource.local.CarLocalDatasource
import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasource
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.data.parser.CarNetworkJsonParser
import com.cool.element.foobar.data.parser.CarNetworkJsonParserI
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
    abstract fun bindCarLocalDataSource(
        carLocalDatasource: CarLocalDatasource
    ): CarLocalDatasourceI

    @Binds
    @Singleton
    abstract fun bindCarNetworkDataSource(
        carNetworkDataSource: CarNetworkDatasource
    ): CarNetworkDatasourceI

    @Binds
    @Singleton
    abstract fun bindCarNetworkJsonParser(
        carNetworkJsonParser: CarNetworkJsonParser
    ): CarNetworkJsonParserI
}