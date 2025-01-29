package org.example.onlinestoreapi.controllers

import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.services.ProductService
import org.example.onlinestoreapi.strategies.DiscountStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductsController(
    private val productService: ProductService,
    @Autowired
    private val discountStrategy: DiscountStrategy
) {

    @GetMapping("/")
    fun sayHello(): String {
        return "Hello world!"
    }

    @GetMapping("/products")
    fun getProductList(): List<Product> {
        return productService.getAllProducts()
    }

    @PostMapping("/products")
    fun addProduct(@RequestBody product: Product): Product {
        return productService.addProduct(product)
    }
}
