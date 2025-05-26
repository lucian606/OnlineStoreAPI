package org.example.onlinestoreapi.e2e

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.entities.ProductCategory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    val objectMapper: JsonMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .build()

    @Test
    fun contextLoads() {}

    @Test
    fun `GET on root path should return 200`() {
        val response = restTemplate.getForEntity("http://localhost:$port/", String::class.java)
        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body!!.contains("Let's buy some products!"))
    }

    @Test
    fun `GET products should return an empty list if db is empty`() {
        val response = restTemplate.getForEntity("http://localhost:$port/products", List::class.java)
        assertTrue(response.statusCode.is2xxSuccessful)
        assertTrue(response.body!!.isEmpty())
    }

    @Test
    fun `GET product by ID should return 404 if product does not exist`() {
        val uuid = UUID.randomUUID()
        val response = restTemplate.getForEntity("http://localhost:$port/product/$uuid", Product::class.java)
        assertTrue(response.statusCode.is4xxClientError)
    }

    @Test
    fun `POST products should add an item to the db`() {
        val uuid = UUID.randomUUID()
        val postResponse = restTemplate.postForEntity(
            "http://localhost:$port/products",
            mapOf(
                "id" to uuid,
                "name" to "name",
                "category" to "MEAT",
                "expirationDate" to "2021-01-01",
                "price" to 100.0
            ),
            String::class.java)
        assertTrue(postResponse.statusCode.is2xxSuccessful)

        val expectedProduct = Product(
            uuid,
            "name",
            ProductCategory.MEAT,
            LocalDate.ofInstant(Instant.parse("2021-01-01T00:00:00Z"), Clock.systemUTC().zone),
            100.00
        )

        val getResponse = restTemplate.getForEntity("http://localhost:$port/products", List::class.java)
        assertTrue(getResponse.statusCode.is2xxSuccessful)
        assertEquals(1, getResponse.body!!.size)

        val product = objectMapper.convertValue(getResponse.body!![0], Product::class.java)
        assertEquals(expectedProduct, product)
    }

    @Test
    fun `DELETE product should delete an item from the db`() {
        val uuid = UUID.randomUUID()
        val postResponse = restTemplate.postForEntity(
            "http://localhost:$port/products",
            mapOf(
                "id" to uuid,
                "name" to "name",
                "category" to "MEAT",
                "expirationDate" to "2021-01-01",
                "price" to 100.0
            ),
            String::class.java)
        assertTrue(postResponse.statusCode.is2xxSuccessful)

        val expectedProduct = Product(
            uuid,
            "name",
            ProductCategory.MEAT,
            LocalDate.ofInstant(Instant.parse("2021-01-01T00:00:00Z"), Clock.systemUTC().zone),
            100.00
        )

        val getResponse = restTemplate.getForEntity("http://localhost:$port/product/$uuid", Product::class.java)
        assertTrue(getResponse.statusCode.is2xxSuccessful)
        val product = objectMapper.convertValue(getResponse.body, Product::class.java)
        assertEquals(expectedProduct, product)

        val deleteResponse = restTemplate.exchange(
            "http://localhost:$port/product/$uuid",
            org.springframework.http.HttpMethod.DELETE,
            null,
            Map::class.java
        )
        assertTrue(deleteResponse.statusCode.is2xxSuccessful)

        val notFoundResponse = restTemplate.getForEntity("http://localhost:$port/product/$uuid", Product::class.java)
        assertTrue(notFoundResponse.statusCode.is4xxClientError)
    }

    @Test
    fun `DELETE product should return 404 if product does not exist`() {
        val uuid = UUID.randomUUID()
        val deleteResponse = restTemplate.exchange(
            "http://localhost:$port/product/$uuid",
            org.springframework.http.HttpMethod.DELETE,
            null,
            Map::class.java
        )
        assertTrue(deleteResponse.statusCode.is4xxClientError)
    }

    @Test
    fun `PUT product should update it`() {
        val uuid = UUID.randomUUID()
        val postResponse = restTemplate.postForEntity(
            "http://localhost:$port/products",
            mapOf(
                "id" to uuid,
                "name" to "name",
                "category" to "MEAT",
                "expirationDate" to "2021-01-01",
                "price" to 100.0
            ),
            String::class.java)
        assertTrue(postResponse.statusCode.is2xxSuccessful)

        val updatedProduct = mapOf(
            "id" to uuid,
            "name" to "updatedName",
            "category" to "CHEESE",
            "expirationDate" to "2022-01-01",
            "price" to 150.0
        )
        val putResponse = restTemplate.exchange(
            "http://localhost:$port/product/$uuid",
            org.springframework.http.HttpMethod.PUT,
            org.springframework.http.HttpEntity(updatedProduct),
            Product::class.java
        )
        assertTrue(putResponse.statusCode.is2xxSuccessful)

        val getResponse = restTemplate.getForEntity("http://localhost:$port/product/$uuid", Product::class.java)
        assertTrue(getResponse.statusCode.is2xxSuccessful)
        val product = objectMapper.convertValue(getResponse.body, Product::class.java)
        val expectedProduct = Product(
            uuid,
            "updatedName",
            ProductCategory.CHEESE,
            LocalDate.ofInstant(Instant.parse("2022-01-01T00:00:00Z"), Clock.systemUTC().zone),
            150.00
        )
        assertEquals(expectedProduct, product)
    }

    @Test
    fun `PUT product should return 404 if product does not exist`() {
        val uuid = UUID.randomUUID()
        val updatedProduct = mapOf(
            "id" to uuid,
            "name" to "updatedName",
            "category" to "CHEESE",
            "expirationDate" to "2022-01-01",
            "price" to 150.0
        )
        val putResponse = restTemplate.exchange(
            "http://localhost:$port/product/$uuid",
            org.springframework.http.HttpMethod.PUT,
            org.springframework.http.HttpEntity(updatedProduct),
            Product::class.java
        )
        assertTrue(putResponse.statusCode.is4xxClientError)
    }

    @Test
    fun `GET product price should return the price of the product`() {
        val uuid = UUID.randomUUID()
        val postResponse = restTemplate.postForEntity(
            "http://localhost:$port/products",
            mapOf(
                "id" to uuid,
                "name" to "name",
                "category" to "MEAT",
                "expirationDate" to "2021-01-01",
                "price" to 100.0
            ),
            String::class.java)
        assertTrue(postResponse.statusCode.is2xxSuccessful)

        val getPriceResponse = restTemplate.getForEntity("http://localhost:$port/product/$uuid/price", Map::class.java)
        assertTrue(getPriceResponse.statusCode.is2xxSuccessful)
        assertEquals(100.0, getPriceResponse.body!!["price"])
    }

    @Test
    fun `GET product price should return 404 if product does not exist`() {
        val uuid = UUID.randomUUID()
        val getPriceResponse = restTemplate.getForEntity("http://localhost:$port/product/$uuid/price", Map::class.java)
        assertTrue(getPriceResponse.statusCode.is4xxClientError)
    }
}
