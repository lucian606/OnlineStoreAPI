package org.example.onlinestoreapi.strategies

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.example.onlinestoreapi.entities.Product

class ExpirationDiscountStrategy(discountAmount: Long, val expirationLimit: Long) : DiscountStrategy (discountAmount) {
    override fun applyDiscount(product: Product): Product {
        val currentDate = LocalDate.now()
        val daysUntilExpiration = ChronoUnit.DAYS.between(currentDate, product.expirationDate)

        return if (daysUntilExpiration < expirationLimit) {
            product.copy(price = product.price * (100f - discountAmount) / 100)
        } else {
            product
        }
    }
}
