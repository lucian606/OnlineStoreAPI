package org.onlinestoreapi

import org.onlinestoreapi.configs.DiscountStrategyConfig
import org.onlinestoreapi.strategies.CategoryDiscountStrategy
import org.onlinestoreapi.strategies.DiscountStrategy
import org.onlinestoreapi.strategies.ExpirationDiscountStrategy
import org.onlinestoreapi.strategies.StrategyType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class DiscountStrategyFactory(
    @Autowired
    private val discountStrategyConfig: DiscountStrategyConfig
) {
    @Bean
    fun discountStrategy(): DiscountStrategy {
        return when (discountStrategyConfig.strategy) {
            StrategyType.EXPIRATION -> ExpirationDiscountStrategy(discountStrategyConfig.expirationLimit)
            StrategyType.CATEGORY -> CategoryDiscountStrategy(discountStrategyConfig.discountedCategories)
            else -> throw IllegalArgumentException("No strategy: ${discountStrategyConfig.strategy}")
        }
    }

    @Bean
    fun amount(): Int {
        return discountStrategyConfig.amount
    }

    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}
