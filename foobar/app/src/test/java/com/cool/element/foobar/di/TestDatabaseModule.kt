package com.cool.element.foobar.di

import com.cool.element.foobar.data.datasource.local.AppRoomDatabaseA
import com.cool.element.foobar.domain.entity.local.CarDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(): AppRoomDatabaseA = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideCarDao(): CarDao = mockk(relaxed = true)
}