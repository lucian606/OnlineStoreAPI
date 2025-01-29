package org.example.onlinestoreapi.strategies

import org.example.onlinestoreapi.entities.Product

abstract class DiscountStrategy(val discountAmount: Long) {
    abstract fun applyDiscount(product: Product): Product
}
