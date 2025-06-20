package org.onlinestoreapi.unit

import org.onlinestoreapi.entities.Product
import org.onlinestoreapi.entities.ProductCategory
import org.onlinestoreapi.strategies.CategoryDiscountStrategy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

class CategoryDiscountStrategyTest {
    @Test
    fun `should apply discount to products with discounted categories`() {
        val discount = 10
        val product = Product(UUID.randomUUID(), "name", ProductCategory.MEAT, LocalDate.now(), 100.0)
        val discountedCategories = setOf(ProductCategory.MEAT, ProductCategory.CHEESE)

        val strategy = CategoryDiscountStrategy(discountedCategories)
        val expectedProduct = product.copy(price = product.price * (100f - discount) / 100)
        assertEquals(expectedProduct, strategy.applyDiscount(product, discount))
    }

    @Test
    fun `should not apply discount to products outside discounted categories`() {
        val discount = 10
        val product = Product(UUID.randomUUID(), "name", ProductCategory.FRUIT, LocalDate.now(), 100.0)
        val discountedCategories = setOf(ProductCategory.MEAT, ProductCategory.CHEESE)

        val strategy = CategoryDiscountStrategy(discountedCategories)
        assertEquals(product, strategy.applyDiscount(product, discount))
    }
}