package org.example.onlinestoreapi.controllers

import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.services.ProductService
import org.example.onlinestoreapi.strategies.DiscountStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ProductsController(
    private val productService: ProductService,
    @Autowired
    private val discountStrategy: DiscountStrategy,
    @Autowired
    private val amount: Int
) {

    @GetMapping("/")
    fun sayHello(): String {
        return "Give me some products to sell!"
    }

    @GetMapping("/products")
    fun getProductList(@RequestParam(required = false) discounted: Boolean): List<Product> {
        if (discounted) {
            return productService.getAllProducts().map { discountStrategy.applyDiscount(it, amount) }
        }
        return productService.getAllProducts()
    }

    @GetMapping("/product/{id}/price")
    fun getProductPrice(@PathVariable id: UUID): Double {
        return productService.getProductById(id).price
    }

    @PostMapping("/products")
    fun addProduct(@RequestBody product: Product): Product {
        return productService.addProduct(product)
    }

    @DeleteMapping("/product/{id}")
    fun deleteProductById(@PathVariable id: UUID) {
        productService.deleteProductById(id)
    }
}
