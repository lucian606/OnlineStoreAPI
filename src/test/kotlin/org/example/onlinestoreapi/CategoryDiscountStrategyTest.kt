package org.example.onlinestoreapi

import io.mockk.junit5.MockKExtension
import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.entities.ProductCategory
import org.example.onlinestoreapi.strategies.CategoryDiscountStrategy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class CategoryDiscountStrategyTest {
    @Test
    fun `should apply discount to products with discounted categories`() {
        val discount = 10
        val product = Product(UUID.randomUUID(), "name", ProductCategory.MEAT, LocalDate.now(), 100.0)
        val expectedProduct = product.copy(price = product.price * (100f - discount) / 100)
        val discountedCategories = setOf(ProductCategory.MEAT, ProductCategory.CHEESE)

        val strategy = CategoryDiscountStrategy(discountedCategories)
        assertEquals(expectedProduct, strategy.applyDiscount(product, discount))
    }
}