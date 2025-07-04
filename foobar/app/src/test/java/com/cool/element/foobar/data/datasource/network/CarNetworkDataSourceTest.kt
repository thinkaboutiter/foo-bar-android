package com.cool.element.foobar.data.datasource.network

import com.cool.element.foobar.data.datasource.network.webservice.CarWebServiceI
import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.cool.element.foobar.domain.entity.network.CarNetworkResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CarNetworkDataSourceTest {

    private lateinit var dataSource: CarNetworkDataSource
    private val mockWebService: CarWebServiceI = mockk()

    @Before
    fun setup() {
        dataSource = CarNetworkDataSource(mockWebService)
    }

    @Test
    fun `fetchCars should return cars from successful response`() = runTest {
        // Given
        val cars = listOf(
            CarNetwork(
                title = "Test Car 1",
                aboutUrlString = "https://test1.com",
                details = "Test car 1 details",
                imageUrlString = "https://test1.com/image.jpg"
            ),
            CarNetwork(
                title = "Test Car 2",
                aboutUrlString = "https://test2.com",
                details = "Test car 2 details",
                imageUrlString = "https://test2.com/image.jpg"
            )
        )
        val response = CarNetworkResponse(
            title = "Test Response",
            version = "1.0",
            cars = cars
        )
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns response
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Test Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://test1.com")
        assertThat(result[0].details).isEqualTo("Test car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://test1.com/image.jpg")
        
        assertThat(result[1].title).isEqualTo("Test Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://test2.com")
        assertThat(result[1].details).isEqualTo("Test car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://test2.com/image.jpg")
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should return empty list when response body contains empty cars list`() = runTest {
        // Given
        val response = CarNetworkResponse(
            title = "Empty Response",
            version = "1.0",
            cars = emptyList()
        )
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns response
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).isEmpty()
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should throw exception when response is not successful`() = runTest {
        // Given
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns false
        coEvery { mockResponse.code() } returns 404
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When & Then
        try {
            dataSource.fetchCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Network call failed with code: 404")
        }
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should throw exception when response body is null`() = runTest {
        // Given
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns null
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When & Then
        try {
            dataSource.fetchCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Response body is null")
        }
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should throw exception when web service throws exception`() = runTest {
        // Given
        val expectedException = RuntimeException("Network error")
        coEvery { mockWebService.getCarNetworkResponse() } throws expectedException

        // When & Then
        try {
            dataSource.fetchCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should handle HTTP error codes correctly`() = runTest {
        // Given
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns false
        coEvery { mockResponse.code() } returns 500
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When & Then
        try {
            dataSource.fetchCars()
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("Network call failed with code: 500")
        }
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should handle single car response correctly`() = runTest {
        // Given
        val singleCar = listOf(
            CarNetwork(
                title = "Single Car",
                aboutUrlString = "https://single.com",
                details = "Single car details",
                imageUrlString = "https://single.com/image.jpg"
            )
        )
        val response = CarNetworkResponse(
            title = "Single Response",
            version = "1.0",
            cars = singleCar
        )
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns response
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single.com")
        assertThat(result[0].details).isEqualTo("Single car details")
        assertThat(result[0].imageUrlString).isEqualTo("https://single.com/image.jpg")
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }

    @Test
    fun `fetchCars should handle cars with empty values correctly`() = runTest {
        // Given
        val carsWithEmpty = listOf(
            CarNetwork(
                title = "",
                aboutUrlString = "",
                details = "",
                imageUrlString = ""
            )
        )
        val response = CarNetworkResponse(
            title = "Empty Response",
            version = "1.0",
            cars = carsWithEmpty
        )
        val mockResponse = mockk<Response<CarNetworkResponse>>()
        coEvery { mockResponse.isSuccessful } returns true
        coEvery { mockResponse.body() } returns response
        coEvery { mockWebService.getCarNetworkResponse() } returns mockResponse

        // When
        val result = dataSource.fetchCars()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEmpty()
        assertThat(result[0].aboutUrlString).isEmpty()
        assertThat(result[0].details).isEmpty()
        assertThat(result[0].imageUrlString).isEmpty()
        
        coVerify { mockWebService.getCarNetworkResponse() }
    }
}