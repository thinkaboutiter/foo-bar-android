package com.cool.element.foobar.data.repository

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepositoryStrategyTest {

    @Test
    fun `RepositoryStrategy should have NETWORK value`() {
        // When
        val strategy = RepositoryStrategy.NETWORK

        // Then
        assertThat(strategy).isEqualTo(RepositoryStrategy.NETWORK)
        assertThat(strategy.name).isEqualTo("NETWORK")
    }

    @Test
    fun `RepositoryStrategy should have LOCAL value`() {
        // When
        val strategy = RepositoryStrategy.LOCAL

        // Then
        assertThat(strategy).isEqualTo(RepositoryStrategy.LOCAL)
        assertThat(strategy.name).isEqualTo("LOCAL")
    }

    @Test
    fun `RepositoryStrategy should have exactly two values`() {
        // When
        val values = RepositoryStrategy.values()

        // Then
        assertThat(values).hasLength(2)
        assertThat(values).asList().containsExactly(RepositoryStrategy.NETWORK, RepositoryStrategy.LOCAL)
    }

    @Test
    fun `RepositoryStrategy valueOf should work correctly`() {
        // When & Then
        assertThat(RepositoryStrategy.valueOf("NETWORK")).isEqualTo(RepositoryStrategy.NETWORK)
        assertThat(RepositoryStrategy.valueOf("LOCAL")).isEqualTo(RepositoryStrategy.LOCAL)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `RepositoryStrategy valueOf should throw exception for invalid value`() {
        // When & Then
        RepositoryStrategy.valueOf("INVALID")
    }

    @Test
    fun `RepositoryStrategy ordinal values should be correct`() {
        // When & Then
        assertThat(RepositoryStrategy.NETWORK.ordinal).isEqualTo(0)
        assertThat(RepositoryStrategy.LOCAL.ordinal).isEqualTo(1)
    }

    @Test
    fun `RepositoryStrategy should support equality comparison`() {
        // Given
        val strategy1 = RepositoryStrategy.NETWORK
        val strategy2 = RepositoryStrategy.NETWORK
        val strategy3 = RepositoryStrategy.LOCAL

        // When & Then
        assertThat(strategy1).isEqualTo(strategy2)
        assertThat(strategy1).isNotEqualTo(strategy3)
        assertThat(strategy1.hashCode()).isEqualTo(strategy2.hashCode())
    }

    @Test
    fun `RepositoryStrategy should support when expression`() {
        // Given
        val networkStrategy = RepositoryStrategy.NETWORK
        val localStrategy = RepositoryStrategy.LOCAL

        // When
        val networkResult = when (networkStrategy) {
            RepositoryStrategy.NETWORK -> "network"
            RepositoryStrategy.LOCAL -> "local"
        }

        val localResult = when (localStrategy) {
            RepositoryStrategy.NETWORK -> "network"
            RepositoryStrategy.LOCAL -> "local"
        }

        // Then
        assertThat(networkResult).isEqualTo("network")
        assertThat(localResult).isEqualTo("local")
    }
}
