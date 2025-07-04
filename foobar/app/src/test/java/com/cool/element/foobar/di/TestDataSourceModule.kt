package com.cool.element.foobar.di

import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.data.parser.CarNetworkJsonParserI
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
object TestDataSourceModule {

    @Provides
    @Singleton
    fun provideCarLocalDataSource(): CarLocalDatasourceI = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideCarNetworkDataSource(): CarNetworkDatasourceI = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideCarNetworkJsonParser(): CarNetworkJsonParserI = mockk(relaxed = true)
}