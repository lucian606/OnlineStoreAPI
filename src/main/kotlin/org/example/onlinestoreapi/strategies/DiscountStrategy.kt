package org.example.onlinestoreapi.strategies

import org.example.onlinestoreapi.entities.Product

fun interface DiscountStrategy {
    fun applyDiscount(product: Product, discount: Long): Product
}
