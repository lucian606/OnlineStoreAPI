package org.onlinestoreapi.configs

import org.onlinestoreapi.entities.ProductCategory
import org.onlinestoreapi.strategies.StrategyType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "discount")
data class DiscountStrategyConfig(
    var strategy: StrategyType = StrategyType.NONE,
    var amount: Int = 0,
    var expirationLimit: Long = 0,
    var discountedCategories: Set<ProductCategory> = mutableSetOf()
)