package org.example.onlinestoreapi.strategies

import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.entities.ProductCategory

class CategoryDiscountStrategy(discountAmount: Long, val discountedCategories: Set<ProductCategory>) : DiscountStrategy(discountAmount) {
    override fun applyDiscount(product: Product): Product {
        return if (product.category in discountedCategories) {
            product.copy(price = product.price * (100f - discountAmount) / 100)
        } else {
            product
        }
    }
}
