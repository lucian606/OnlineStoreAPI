package org.example.onlinestoreapi.strategies

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.example.onlinestoreapi.entities.Product
import org.springframework.beans.factory.annotation.Autowired
import java.time.Clock

class ExpirationDiscountStrategy(val expirationLimit: Long) : DiscountStrategy {
    @Autowired
    private var clock: Clock = Clock.systemUTC()

    override fun applyDiscount(product: Product, discount: Int): Product {
        val currentDate = LocalDate.now(clock)
        println(currentDate)
        val daysUntilExpiration = ChronoUnit.DAYS.between(currentDate, product.expirationDate)

        return if (daysUntilExpiration < expirationLimit) {
            product.copy(price = product.price * (100f - discount) / 100)
        } else {
            product
        }
    }
}
