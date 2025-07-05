package com.cool.element.foobar.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cool.element.foobar.data.repository.CarRepository
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.cool.element.foobar.data.repository.RepositoryStrategy
import com.cool.element.foobar.data.datasource.local.CarLocalDatasourceI
import com.cool.element.foobar.data.datasource.network.CarNetworkDatasourceI
import com.cool.element.foobar.domain.entity.application.CarApp
import com.cool.element.foobar.domain.entity.local.CarLocal
import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.cool.element.foobar.presentation.view.carlist.viewmodel.CarListViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.mockk

@ExperimentalCoroutinesApi
class DataFlowIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CarRepository
    private val mockNetworkDatasource: CarNetworkDatasourceI = mockk()
    private val mockLocalDatasource: CarLocalDatasourceI = mockk()

    private lateinit var viewModel: CarListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = CarRepository(mockNetworkDatasource, mockLocalDatasource)
        viewModel = CarListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `end to end flow - network cars should flow from datasource through repository to viewmodel`() = runTest {
        // Given
        val networkCars = listOf(
            CarNetwork(
                title = "Integration Network Car 1",
                aboutUrlString = "https://integration1.com",
                details = "Integration network car 1 details",
                imageUrlString = "https://integration1.com/image.jpg"
            ),
            CarNetwork(
                title = "Integration Network Car 2",
                aboutUrlString = "https://integration2.com",
                details = "Integration network car 2 details",
                imageUrlString = "https://integration2.com/image.jpg"
            )
        )
        coEvery { mockNetworkDatasource.fetchCars() } returns networkCars

        // When
        val result = viewModel.getCars(RepositoryStrategy.NETWORK)

        // Then
        assertThat(result).hasSize(2)
        
        // Verify first car mapping
        assertThat(result[0].title).isEqualTo("Integration Network Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://integration1.com")
        assertThat(result[0].details).isEqualTo("Integration network car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://integration1.com/image.jpg")
        
        // Verify second car mapping
        assertThat(result[1].title).isEqualTo("Integration Network Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://integration2.com")
        assertThat(result[1].details).isEqualTo("Integration network car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://integration2.com/image.jpg")
        
        // Verify the result contains CarApp entities
        result.forEach { car ->
            assertThat(car).isInstanceOf(CarApp::class.java)
        }
    }

    @Test
    fun `end to end flow - local cars should flow from datasource through repository to viewmodel`() = runTest {
        // Given
        val localCars = listOf(
            CarLocal(
                id = 1L,
                title = "Integration Local Car 1",
                aboutUrlString = "https://local-integration1.com",
                details = "Integration local car 1 details",
                imageUrlString = "https://local-integration1.com/image.jpg"
            ),
            CarLocal(
                id = 2L,
                title = "Integration Local Car 2",
                aboutUrlString = "https://local-integration2.com",
                details = "Integration local car 2 details",
                imageUrlString = "https://local-integration2.com/image.jpg"
            )
        )
        coEvery { mockLocalDatasource.getAllCars() } returns localCars

        // When
        val result = viewModel.getCars(RepositoryStrategy.LOCAL)

        // Then
        assertThat(result).hasSize(2)
        
        // Verify first car mapping (note: id is not mapped to CarApp)
        assertThat(result[0].title).isEqualTo("Integration Local Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://local-integration1.com")
        assertThat(result[0].details).isEqualTo("Integration local car 1 details")
        assertThat(result[0].imageUrlString).isEqualTo("https://local-integration1.com/image.jpg")
        
        // Verify second car mapping
        assertThat(result[1].title).isEqualTo("Integration Local Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://local-integration2.com")
        assertThat(result[1].details).isEqualTo("Integration local car 2 details")
        assertThat(result[1].imageUrlString).isEqualTo("https://local-integration2.com/image.jpg")
        
        // Verify the result contains CarApp entities
        result.forEach { car ->
            assertThat(car).isInstanceOf(CarApp::class.java)
        }
    }

    @Test
    fun `end to end flow - error propagation from network datasource to viewmodel`() = runTest {
        // Given
        val expectedException = RuntimeException("Network integration error")
        coEvery { mockNetworkDatasource.fetchCars() } throws expectedException

        // When & Then
        try {
            viewModel.getCars(RepositoryStrategy.NETWORK)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
    }

    @Test
    fun `end to end flow - error propagation from local datasource to viewmodel`() = runTest {
        // Given
        val expectedException = RuntimeException("Local integration error")
        coEvery { mockLocalDatasource.getAllCars() } throws expectedException

        // When & Then
        try {
            viewModel.getCars(RepositoryStrategy.LOCAL)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isEqualTo(expectedException)
        }
    }

    @Test
    fun `end to end flow - empty lists should flow correctly through the stack`() = runTest {
        // Given
        coEvery { mockNetworkDatasource.fetchCars() } returns emptyList()
        coEvery { mockLocalDatasource.getAllCars() } returns emptyList()

        // When
        val networkResult = viewModel.getCars(RepositoryStrategy.NETWORK)
        val localResult = viewModel.getCars(RepositoryStrategy.LOCAL)

        // Then
        assertThat(networkResult).isEmpty()
        assertThat(localResult).isEmpty()
    }


    @Test
    fun `integration test - entity mapping consistency`() = runTest {
        // Given
        val networkCar = CarNetwork(
            title = "Mapping Test Car",
            aboutUrlString = "https://mapping-test.com",
            details = "Mapping test details",
            imageUrlString = "https://mapping-test.com/image.jpg"
        )

        val localCar = CarLocal(
            id = 1L,
            title = "Mapping Test Car",
            aboutUrlString = "https://mapping-test.com",
            details = "Mapping test details",
            imageUrlString = "https://mapping-test.com/image.jpg"
        )

        // When
        val fromNetwork = CarApp.fromCarNetwork(networkCar)
        val fromLocal = CarApp.fromCarLocal(localCar)

        // Then
        assertThat(fromNetwork.title).isEqualTo(fromLocal.title)
        assertThat(fromNetwork.aboutUrlString).isEqualTo(fromLocal.aboutUrlString)
        assertThat(fromNetwork.details).isEqualTo(fromLocal.details)
        assertThat(fromNetwork.imageUrlString).isEqualTo(fromLocal.imageUrlString)
        
        // The mapped entities should be equal except for the ID which is not part of CarApp
        assertThat(fromNetwork).isEqualTo(fromLocal)
    }

    @Test
    fun `integration test - strategy pattern correctness`() = runTest {
        // Given
        val networkCars = listOf(
            CarNetwork(
                title = "Network Strategy Car",
                aboutUrlString = "https://network-strategy.com",
                details = "Network strategy details",
                imageUrlString = "https://network-strategy.com/image.jpg"
            )
        )

        val localCars = listOf(
            CarLocal(
                id = 1L,
                title = "Local Strategy Car",
                aboutUrlString = "https://local-strategy.com",
                details = "Local strategy details",
                imageUrlString = "https://local-strategy.com/image.jpg"
            )
        )

        coEvery { mockNetworkDatasource.fetchCars() } returns networkCars
        coEvery { mockLocalDatasource.getAllCars() } returns localCars

        // When
        val networkResult = viewModel.getCars(RepositoryStrategy.NETWORK)
        val localResult = viewModel.getCars(RepositoryStrategy.LOCAL)

        // Then
        assertThat(networkResult[0].title).isEqualTo("Network Strategy Car")
        assertThat(localResult[0].title).isEqualTo("Local Strategy Car")
        
        // Verify strategies produce different results
        assertThat(networkResult).isNotEqualTo(localResult)
    }
}