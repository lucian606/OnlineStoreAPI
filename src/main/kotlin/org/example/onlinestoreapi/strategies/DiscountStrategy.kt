package org.example.onlinestoreapi.strategies

import org.example.onlinestoreapi.entities.Product

// Todo: Replace abstract class with functional interface
abstract class DiscountStrategy(val discountAmount: Long) {
    abstract fun applyDiscount(product: Product): Product
}
