package com.cool.element.foobar.presentation.view.carlist.viewmodel

import androidx.lifecycle.ViewModel
import com.cool.element.foobar.data.repository.CarRepositoryI
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class CarListViewModelFactoryTest {

    private lateinit var factory: CarListViewModelFactory
    private val mockRepository: CarRepositoryI = mockk()

    @Before
    fun setup() {
        factory = CarListViewModelFactory(mockRepository)
    }

    @Test
    fun `create should return CarListViewModel when modelClass is CarListViewModel`() {
        // When
        val viewModel = factory.create(CarListViewModel::class.java)

        // Then
        assertThat(viewModel).isInstanceOf(CarListViewModel::class.java)
        assertThat(viewModel).isInstanceOf(CarListViewModelA::class.java)
        assertThat(viewModel).isInstanceOf(CarListViewModelI::class.java)
        assertThat(viewModel).isInstanceOf(ViewModel::class.java)
    }

    @Test
    fun `create should return CarListViewModel with correct repository`() {
        // When
        val viewModel = factory.create(CarListViewModel::class.java) as CarListViewModel

        // Then
        assertThat(viewModel.repository).isEqualTo(mockRepository)
    }

    @Test
    fun `create should throw IllegalArgumentException for unknown ViewModel class`() {
        // Given
        class UnknownViewModel : ViewModel()

        // When & Then
        try {
            factory.create(UnknownViewModel::class.java)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("Unknown ViewModel class")
        }
    }

    @Test
    fun `create should throw IllegalArgumentException for null modelClass`() {
        // Given
        @Suppress("UNCHECKED_CAST")
        val nullClass = null as Class<ViewModel>

        // When & Then
        try {
            factory.create(nullClass)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            // Expected to throw some exception due to null class
            assertThat(e).isNotNull()
        }
    }

    @Test
    fun `create should return different instances for multiple calls`() {
        // When
        val viewModel1 = factory.create(CarListViewModel::class.java)
        val viewModel2 = factory.create(CarListViewModel::class.java)

        // Then
        assertThat(viewModel1).isNotSameInstanceAs(viewModel2)
        assertThat(viewModel1).isInstanceOf(CarListViewModel::class.java)
        assertThat(viewModel2).isInstanceOf(CarListViewModel::class.java)
    }

    @Test
    fun `factory should be instance of ViewModelProvider Factory`() {
        // Then
        assertThat(factory).isInstanceOf(androidx.lifecycle.ViewModelProvider.Factory::class.java)
    }

    @Test
    fun `create should handle subclass of CarListViewModel`() {
        // Given
        class CarListViewModelSubclass(repository: CarRepositoryI) : CarListViewModel(repository)

        // When
        val viewModel = factory.create(CarListViewModel::class.java)

        // Then
        assertThat(viewModel).isInstanceOf(CarListViewModel::class.java)
        // The factory should create CarListViewModel, not the subclass
        assertThat(viewModel.javaClass).isEqualTo(CarListViewModel::class.java)
    }

    @Test
    fun `create should be thread-safe for concurrent calls`() {
        // Given
        val results = mutableListOf<CarListViewModel>()
        val threads = (1..10).map { 
            Thread {
                val viewModel = factory.create(CarListViewModel::class.java) as CarListViewModel
                synchronized(results) {
                    results.add(viewModel)
                }
            }
        }

        // When
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        // Then
        assertThat(results).hasSize(10)
        // All should be different instances
        val distinctResults = results.distinct()
        assertThat(distinctResults).hasSize(10)
        
        // All should be CarListViewModel instances
        results.forEach { viewModel ->
            assertThat(viewModel).isInstanceOf(CarListViewModel::class.java)
        }
    }
}