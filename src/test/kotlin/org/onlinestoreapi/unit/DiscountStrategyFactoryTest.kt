package org.onlinestoreapi.unit

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.onlinestoreapi.DiscountStrategyFactory
import org.onlinestoreapi.configs.DiscountStrategyConfig
import org.onlinestoreapi.strategies.ExpirationDiscountStrategy
import org.onlinestoreapi.strategies.StrategyType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class DiscountStrategyFactoryTest {

    val discountStrategyConfig = mockk<DiscountStrategyConfig>()

    @InjectMockKs
    lateinit var factory: DiscountStrategyFactory

    @Test
    fun `should return EXPIRATION discount strategy`() {
        every { discountStrategyConfig.strategy } returns StrategyType.EXPIRATION
        every { discountStrategyConfig.expirationLimit } returns 5

        val strategy = factory.discountStrategy()
        assertTrue(strategy is ExpirationDiscountStrategy)
        assertEquals(5, strategy.expirationLimit)
    }

    @Test
    fun `should return CATEGORY discount strategy`() {
        every { discountStrategyConfig.strategy } returns StrategyType.CATEGORY
        every { discountStrategyConfig.discountedCategories } returns setOf()

        val strategy = factory.discountStrategy()
        assertTrue(strategy is org.onlinestoreapi.strategies.CategoryDiscountStrategy)
        assertTrue(strategy.discountedCategories.isEmpty())
    }

    @Test
    fun `should throw exception for unknown strategy`() {
        every { discountStrategyConfig.strategy } returns StrategyType.NONE

        assertThrows<IllegalArgumentException> {
            factory.discountStrategy()
        }
    }

}