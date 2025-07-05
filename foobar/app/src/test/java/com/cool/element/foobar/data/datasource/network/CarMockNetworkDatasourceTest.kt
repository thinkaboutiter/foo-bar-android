package com.cool.element.foobar.data.datasource.network

import android.content.Context
import com.cool.element.foobar.R
import com.cool.element.foobar.data.parser.CarNetworkJsonParserI
import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CarMockNetworkDatasourceTest {

    private lateinit var dataSource: CarMockNetworkDatasource
    private val mockParser: CarNetworkJsonParserI = mockk()

    private val mockContext: Context = mockk()

    @Before
    fun setup() {
        dataSource = CarMockNetworkDatasource(mockContext, mockParser)
    }

    @Test
    fun `fetchCars should return cars from parser`() = runTest {
        // Given
        val expectedCars = listOf(
            CarNetwork(
                title = "Mock Car 1",
                aboutUrlString = "https://mock1.com",
                details = "Mock car 1 details",
                imageUrlString = "https://mock1.com/image.jpg"
            ),
            CarNetwork(
                title = "Mock Car 2",
                aboutUrlString = "https://mock2.com",
                details = "Mock car 2 details",
                imageUrlString = "https://mock2.com/image.jpg"
            )
        )
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns expectedCars

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Mock Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://mock1.com")
        assertThat(result[0].details).isEqualTo("Mock car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://mock1.com/image.jpg")
        
        assertThat(result[1].title).isEqualTo("Mock Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://mock2.com")
        assertThat(result[1].details).isEqualTo("Mock car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://mock2.com/image.jpg")
        
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should return empty list when parser returns empty list`() = runTest {
        // Given
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns emptyList()

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).isEmpty()
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should propagate exception from parser`() = runTest {
        // Given
        val expectedException = RuntimeException("Parser error")
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } throws expectedException

        // When & Then
        try {
            dataSource.fetchCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should handle single car from parser`() = runTest {
        // Given
        val singleCar = listOf(
            CarNetwork(
                title = "Single Mock Car",
                aboutUrlString = "https://single-mock.com",
                details = "Single mock car details",
                imageUrlString = "https://single-mock.com/image.jpg"
            )
        )
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns singleCar

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Mock Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single-mock.com")
        assertThat(result[0].details).isEqualTo("Single mock car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single-mock.com/image.jpg")
        
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should handle cars with empty values from parser`() = runTest {
        // Given
        val carsWithEmpty = listOf(
            CarNetwork(
                title = "",
                aboutUrlString = "",
                details = "",
                imageUrlString = ""
            )
        )
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns carsWithEmpty

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEmpty()
        assertThat(result[0].aboutUrlString).isEmpty()
        assertThat(result[0].details).isEmpty()
        assertThat(result[0].imageUrlString).isEmpty()
        
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should use correct resource ID`() = runTest {
        // Given
        val expectedCars = listOf(
            CarNetwork(
                title = "Resource Test Car",
                aboutUrlString = "https://resource-test.com",
                details = "Resource test car details",
                imageUrlString = "https://resource-test.com/image.jpg"
            )
        )
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns expectedCars

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Resource Test Car")
        
        // Verify the correct resource ID was used
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }

    @Test
    fun `fetchCars should handle large number of cars from parser`() = runTest {
        // Given
        val largeCarslist = (1..100).map { index ->
            CarNetwork(
                title = "Mock Car $index",
                aboutUrlString = "https://mock$index.com",
                details = "Mock car $index details",
                imageUrlString = "https://mock$index.com/image.jpg"
            )
        }
        coEvery { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) } returns largeCarslist

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(100)
        assertThat(result[0].title).isEqualTo("Mock Car 1")
        assertThat(result[99].title).isEqualTo("Mock Car 100")
        
        coVerify { mockParser.parseCarModelsFromRaw(mockContext, R.raw.mockdata) }
    }
}