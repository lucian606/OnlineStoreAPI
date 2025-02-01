package org.example.onlinestoreapi

import org.example.onlinestoreapi.entities.ProductCategory
import org.example.onlinestoreapi.strategies.CategoryDiscountStrategy
import org.example.onlinestoreapi.strategies.DiscountStrategy
import org.example.onlinestoreapi.strategies.ExpirationDiscountStrategy
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// TODO: Replace strategy with ENUM
// TODO: Separate Configuration from ConfigurationProperty using a Data Class for the properties
// TODO: Have a separate Configuration class for the DiscountStrategyChooser
// TODO: Create a package for configs
@Configuration
@ConfigurationProperties(prefix = "discount")
class DiscountStrategyChooser(
    var amount: Long = 0,
    var strategy: String = "NONE",
    var expirationLimit: Long = 0,
    var discountedCategories: Set<ProductCategory> = mutableSetOf()
) {

    @Bean
    fun discountStrategy(): DiscountStrategy {
        return when (strategy) {
            "expiration" -> ExpirationDiscountStrategy(amount, expirationLimit)
            "category" -> CategoryDiscountStrategy(amount, discountedCategories)
            else -> throw IllegalArgumentException("Unknown strategy: $strategy")
        }
    }
}
