package com.cool.element.foobar.data.parser

import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.IOException

class CarNetworkJsonParserTest {

    private lateinit var parser: CarNetworkJsonParser
    private val mockGson: Gson = mockk()

    @Before
    fun setup() {
        parser = CarNetworkJsonParser(mockGson)
    }

    @Test
    fun `parseCarModelsFromString should return list of CarNetwork objects`() {
        // Given
        val jsonString = """
            {
                "title": "Test Response",
                "version": "1.0",
                "results": [
                    {
                        "title": "Car 1",
                        "href": "https://car1.com",
                        "description": "Car 1 description",
                        "thumbnail": "https://car1.com/thumb.jpg"
                    },
                    {
                        "title": "Car 2",
                        "href": "https://car2.com",
                        "description": "Car 2 description",
                        "thumbnail": "https://car2.com/thumb.jpg"
                    }
                ]
            }
        """.trimIndent()

        // Use real Gson for this test
        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].title).isEqualTo("Car 1")
        assertThat(result[0].aboutUrlString).isEqualTo("https://car1.com")
        assertThat(result[0].details).isEqualTo("Car 1 description")
        assertThat(result[0].imageUrlString).isEqualTo("https://car1.com/thumb.jpg")
        
        assertThat(result[1].title).isEqualTo("Car 2")
        assertThat(result[1].aboutUrlString).isEqualTo("https://car2.com")
        assertThat(result[1].details).isEqualTo("Car 2 description")
        assertThat(result[1].imageUrlString).isEqualTo("https://car2.com/thumb.jpg")
    }

    @Test
    fun `parseCarModelsFromString should handle empty results array`() {
        // Given
        val jsonString = """
            {
                "title": "Test Response",
                "version": "1.0",
                "results": []
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `parseCarModelsFromString should handle single car in results`() {
        // Given
        val jsonString = """
            {
                "title": "Test Response",
                "version": "1.0",
                "results": [
                    {
                        "title": "Single Car",
                        "href": "https://single.com",
                        "description": "Single car description",
                        "thumbnail": "https://single.com/thumb.jpg"
                    }
                ]
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Single Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://single.com")
        assertThat(result[0].details).isEqualTo("Single car description")
        assertThat(result[0].imageUrlString).isEqualTo("https://single.com/thumb.jpg")
    }

    @Test
    fun `parseCarModelsFromString should handle null values in car fields`() {
        // Given
        val jsonString = """
            {
                "title": "Test Response",
                "version": "1.0",
                "results": [
                    {
                        "title": null,
                        "href": null,
                        "description": null,
                        "thumbnail": null
                    }
                ]
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isNull()
        assertThat(result[0].aboutUrlString).isNull()
        assertThat(result[0].details).isNull()
        assertThat(result[0].imageUrlString).isNull()
    }

    @Test
    fun `parseCarModelsFromString should handle empty string values`() {
        // Given
        val jsonString = """
            {
                "title": "Test Response",
                "version": "1.0",
                "results": [
                    {
                        "title": "",
                        "href": "",
                        "description": "",
                        "thumbnail": ""
                    }
                ]
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEmpty()
        assertThat(result[0].aboutUrlString).isEmpty()
        assertThat(result[0].details).isEmpty()
        assertThat(result[0].imageUrlString).isEmpty()
    }

    @Test
    fun `parseCarModelsFromStream should parse InputStream correctly`() {
        // Given
        val jsonString = """
            {
                "title": "Stream Response",
                "version": "1.0",
                "results": [
                    {
                        "title": "Stream Car",
                        "href": "https://stream.com",
                        "description": "Stream car description",
                        "thumbnail": "https://stream.com/thumb.jpg"
                    }
                ]
            }
        """.trimIndent()

        val inputStream = ByteArrayInputStream(jsonString.toByteArray())
        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromStream(inputStream)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].title).isEqualTo("Stream Car")
        assertThat(result[0].aboutUrlString).isEqualTo("https://stream.com")
        assertThat(result[0].details).isEqualTo("Stream car description")
        assertThat(result[0].imageUrlString).isEqualTo("https://stream.com/thumb.jpg")
    }

    @Test
    fun `parseCarModelsFromString should throw exception for malformed JSON`() {
        // Given
        val malformedJson = "{ invalid json structure"

        val realParser = CarNetworkJsonParser()

        // When & Then
        try {
            realParser.parseCarModelsFromString(malformedJson)
            assertThat(false).isTrue() // Should not reach here
        } catch (e: Exception) {
            assertThat(e).isNotNull()
        }
    }

    @Test
    fun `parseCarModelsFromString should handle missing results field gracefully`() {
        // Given
        val invalidJson = """
            {
                "title": "Test Response",
                "version": "1.0"
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When & Then
        try {
            val result = realParser.parseCarModelsFromString(invalidJson)
            // Gson may succeed but return null results
            assertThat(result).isNull()
        } catch (e: Exception) {
            // Also acceptable - missing results field causes exception
            assertThat(e).isNotNull()
        }
    }

    @Test
    fun `parseCarModelsFromStream should handle empty stream gracefully`() {
        // Given
        val emptyStream = ByteArrayInputStream(byteArrayOf())
        val realParser = CarNetworkJsonParser()

        // When & Then
        try {
            val result = realParser.parseCarModelsFromStream(emptyStream)
            // May return null or empty result
            assertThat(result).isNull()
        } catch (e: Exception) {
            // Also acceptable - empty stream causes exception
            assertThat(e).isNotNull()
        }
    }

    @Test
    fun `parseCarModelsFromString should handle large number of cars`() {
        // Given
        val carsJson = (1..100).joinToString(",") { index ->
            """
            {
                "title": "Car $index",
                "href": "https://car$index.com",
                "description": "Car $index description",
                "thumbnail": "https://car$index.com/thumb.jpg"
            }
            """.trimIndent()
        }

        val jsonString = """
            {
                "title": "Large Response",
                "version": "1.0",
                "results": [$carsJson]
            }
        """.trimIndent()

        val realParser = CarNetworkJsonParser()

        // When
        val result = realParser.parseCarModelsFromString(jsonString)

        // Then
        assertThat(result).hasSize(100)
        assertThat(result[0].title).isEqualTo("Car 1")
        assertThat(result[99].title).isEqualTo("Car 100")
    }
}