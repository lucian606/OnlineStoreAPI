package org.onlinestoreapi.strategies

import org.onlinestoreapi.entities.Product

fun interface DiscountStrategy {
    fun applyDiscount(product: Product, discount: Int): Product
}
