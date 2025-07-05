package com.cool.element.foobar.presentation.view.carlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.domain.entity.application.CarApp
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.mockk

@ExperimentalCoroutinesApi
class CarListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: CarRepositoryI = mockk()

    private lateinit var viewModel: CarListViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CarListViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCars with NETWORK strategy should return cars from repository getNetworkCars`() = runTest {
        // Given
        val expectedCars = listOf(
            CarApp(
                title = "Network Car 1",
                aboutUrlString = "https://network1.com",
                details = "Network car 1 details",
                imageUrlString = "https://network1.com/image.jpg"
            ),
            CarApp(
                title = "Network Car 2",
                aboutUrlString = "https://network2.com",
                details = "Network car 2 details",
                imageUrlString = "https://network2.com/image.jpg"
            )
        )
        coEvery { mockRepository.getNetworkCars() } returns expectedCars

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

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
        
        coVerify { mockRepository.getNetworkCars() }
        coVerify(exactly = 0) { mockRepository.getLocalCars() }
    }

    @Test
    fun `getCars with LOCAL strategy should return cars from repository getLocalCars`() = runTest {
        // Given
        val expectedCars = listOf(
            CarApp(
                title = "Local Car 1",
                aboutUrlString = "https://local1.com",
                details = "Local car 1 details",
                imageUrlString = "https://local1.com/image.jpg"
            ),
            CarApp(
                title = "Local Car 2",
                aboutUrlString = "https://local2.com",
                details = "Local car 2 details",
                imageUrlString = "https://local2.com/image.jpg"
            )
        )
        coEvery { mockRepository.getLocalCars() } returns expectedCars

        // When
        val result = viewModel.getCars(RepositoryStrategy.LOCAL)

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
        
        coVerify { mockRepository.getLocalCars() }
        coVerify(exactly = 0) { mockRepository.getNetworkCars() }
    }

    @Test
    fun `getCars with NETWORK strategy should return empty list when repository returns empty list`() = runTest {
        // Given
        coEvery { mockRepository.getNetworkCars() } returns emptyList()

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

        // Then
        assertThat(result).isEmpty()
        coVerify { mockRepository.getNetworkCars() }
    }

    @Test
    fun `getCars with LOCAL strategy should return empty list when repository returns empty list`() = runTest {
        // Given
        coEvery { mockRepository.getLocalCars() } returns emptyList()

        // When
        val result = viewModel.getCars(RepositoryStrategy.LOCAL)

        // Then
        assertThat(result).isEmpty()
        coVerify { mockRepository.getLocalCars() }
    }

    @Test
    fun `getCars with NETWORK strategy should propagate exception from repository`() = runTest {
        // Given
        val expectedException = RuntimeException("Network repository error")
        coEvery { mockRepository.getNetworkCars() } throws expectedException

        // When & Then
        try {
            viewModel.getCars(RepositoryStrategy.NETWORK)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockRepository.getNetworkCars() }
    }

    @Test
    fun `getCars with LOCAL strategy should propagate exception from repository`() = runTest {
        // Given
        val expectedException = RuntimeException("Local repository error")
        coEvery { mockRepository.getLocalCars() } throws expectedException

        // When & Then
        try {
            viewModel.getCars(RepositoryStrategy.LOCAL)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockRepository.getLocalCars() }
    }

    @Test
    fun `getCars with NETWORK strategy should handle single car correctly`() = runTest {
        // Given
        val singleCar = listOf(
            CarApp(
                title = "Single Network Car",
                aboutUrlString = "https://single-network.com",
                details = "Single network car details",
                imageUrlString = "https://single-network.com/image.jpg"
            )
        )
        coEvery { mockRepository.getNetworkCars() } returns singleCar

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Network Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single-network.com")
        assertThat(result[0].details).isEqualTo("Single network car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single-network.com/image.jpg")
        
        coVerify { mockRepository.getNetworkCars() }
    }

    @Test
    fun `getCars with LOCAL strategy should handle single car correctly`() = runTest {
        // Given
        val singleCar = listOf(
            CarApp(
                title = "Single Local Car",
                aboutUrlString = "https://single-local.com",
                details = "Single local car details",
                imageUrlString = "https://single-local.com/image.jpg"
            )
        )
        coEvery { mockRepository.getLocalCars() } returns singleCar

        // When
        val result = viewModel.getCars(RepositoryStrategy.LOCAL)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Local Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single-local.com")
        assertThat(result[0].details).isEqualTo("Single local car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single-local.com/image.jpg")
        
        coVerify { mockRepository.getLocalCars() }
    }

    @Test
    fun `getCars should handle cars with null values correctly`() = runTest {
        // Given
        val carsWithNulls = listOf(
            CarApp(
                title = "",
                aboutUrlString = "",
                details = "",
                imageUrlString = ""
            )
        )
        coEvery { mockRepository.getNetworkCars() } returns carsWithNulls

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEmpty()
        assertThat(result[0].aboutUrlString).isEmpty()
        assertThat(result[0].details).isEmpty()
        assertThat(result[0].imageUrlString).isEmpty()
        
        coVerify { mockRepository.getNetworkCars() }
    }

    @Test
    fun `getCars should handle large number of cars correctly`() = runTest {
        // Given
        val largeCarslist = (1..100).map { index ->
            CarApp(
                title = "Car $index",
                aboutUrlString = "https://car$index.com",
                details = "Car $index details",
                imageUrlString = "https://car$index.com/image.jpg"
            )
        }
        coEvery { mockRepository.getNetworkCars() } returns largeCarslist

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

        // Then
        assertThat(result).hasSize(100)
        assertThat(result[0].title).isEqualTo("Car 1")
        assertThat(result[99].title).isEqualTo("Car 100")
        
        coVerify { mockRepository.getNetworkCars() }
    }

    @Test
    fun `viewModel should be instance of ViewModel`() {
        // Then
        assertThat(viewModel).isInstanceOf(CarListViewModelA::class.java)
        assertThat(viewModel).isInstanceOf(CarListViewModelI::class.java)
    }
}
