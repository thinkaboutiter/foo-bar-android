package com.cool.element.foobar.data.repository

import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.domain.entity.application.CarApp
import com.cool.element.foobar.domain.entity.local.CarLocal
import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.mockk

class CarRepositoryTest {

    private lateinit var repository: CarRepository
    private val mockNetworkDatasource: CarNetworkDatasourceI = mockk()
    private val mockLocalDatasource: CarLocalDatasourceI = mockk()

    @Before
    fun setup() {
        repository = CarRepository(
            networkDatasource = mockNetworkDatasource,
            localDatasource = mockLocalDatasource
        )
    }

    @Test
    fun `getNetworkCars should return mapped CarApp list from network datasource`() = runTest {
        // Given
        val networkCars = listOf(
            CarNetwork(
                title = "Network Car 1",
                aboutUrlString = "https://network1.com",
                details = "Network car 1 details",
                imageUrlString = "https://network1.com/image.jpg"
            ),
            CarNetwork(
                title = "Network Car 2",
                aboutUrlString = "https://network2.com",
                details = "Network car 2 details",
                imageUrlString = "https://network2.com/image.jpg"
            )
        )
        coEvery { mockNetworkDatasource.fetchCars() } returns networkCars

        // When
        val result = repository.getNetworkCars()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Network Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://network1.com")
        assertThat(result[0].details).isEqualTo("Network car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://network1.com/image.jpg")
        
        assertThat(result[1].title).isEqualTo("Network Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://network2.com")
        assertThat(result[1].details).isEqualTo("Network car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://network2.com/image.jpg")
        
        coVerify { mockNetworkDatasource.fetchCars() }
    }

    @Test
    fun `getLocalCars should return mapped CarApp list from local datasource`() = runTest {
        // Given
        val localCars = listOf(
            CarLocal(
                id = 1L,
                title = "Local Car 1",
                aboutUrlString = "https://local1.com",
                details = "Local car 1 details",
                imageUrlString = "https://local1.com/image.jpg"
            ),
            CarLocal(
                id = 2L,
                title = "Local Car 2",
                aboutUrlString = "https://local2.com",
                details = "Local car 2 details",
                imageUrlString = "https://local2.com/image.jpg"
            )
        )
        coEvery { mockLocalDatasource.getAllCars() } returns localCars

        // When
        val result = repository.getLocalCars()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Local Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://local1.com")
        assertThat(result[0].details).isEqualTo("Local car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://local1.com/image.jpg")
        
        assertThat(result[1].title).isEqualTo("Local Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://local2.com")
        assertThat(result[1].details).isEqualTo("Local car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://local2.com/image.jpg")
        
        coVerify { mockLocalDatasource.getAllCars() }
    }

    @Test
    fun `getNetworkCars should return empty list when network datasource returns empty list`() = runTest {
        // Given
        coEvery { mockNetworkDatasource.fetchCars() } returns emptyList()

        // When
        val result = repository.getNetworkCars()

        // Then
        assertThat(result).isEmpty()
        coVerify { mockNetworkDatasource.fetchCars() }
    }

    @Test
    fun `getLocalCars should return empty list when local datasource returns empty list`() = runTest {
        // Given
        coEvery { mockLocalDatasource.getAllCars() } returns emptyList()

        // When
        val result = repository.getLocalCars()

        // Then
        assertThat(result).isEmpty()
        coVerify { mockLocalDatasource.getAllCars() }
    }

    @Test
    fun `getNetworkCars should propagate exception from network datasource`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        coEvery { mockNetworkDatasource.fetchCars() } throws expectedException

        // When & Then
        try {
            repository.getNetworkCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockNetworkDatasource.fetchCars() }
    }

    @Test
    fun `getLocalCars should propagate exception from local datasource`() = runTest {
        // Given
        val expectedException = RuntimeException("Database error")
        coEvery { mockLocalDatasource.getAllCars() } throws expectedException

        // When & Then
        try {
            repository.getLocalCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockLocalDatasource.getAllCars() }
    }

    @Test
    fun `getNetworkCars should handle single car correctly`() = runTest {
        // Given
        val singleNetworkCar = listOf(
            CarNetwork(
                title = "Single Network Car",
                aboutUrlString = "https://single.com",
                details = "Single network car details",
                imageUrlString = "https://single.com/image.jpg"
            )
        )
        coEvery { mockNetworkDatasource.fetchCars() } returns singleNetworkCar

        // When
        val result = repository.getNetworkCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Network Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single.com")
        assertThat(result[0].details).isEqualTo("Single network car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single.com/image.jpg")
        
        coVerify { mockNetworkDatasource.fetchCars() }
    }

    @Test
    fun `getLocalCars should handle single car correctly`() = runTest {
        // Given
        val singleLocalCar = listOf(
            CarLocal(
                id = 1L,
                title = "Single Local Car",
                aboutUrlString = "https://single-local.com",
                details = "Single local car details",
                imageUrlString = "https://single-local.com/image.jpg"
            )
        )
        coEvery { mockLocalDatasource.getAllCars() } returns singleLocalCar

        // When
        val result = repository.getLocalCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Local Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single-local.com")
        assertThat(result[0].details).isEqualTo("Single local car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single-local.com/image.jpg")
        
        coVerify { mockLocalDatasource.getAllCars() }
    }
}