package org.example.onlinestoreapi.services

import java.util.UUID
import org.example.onlinestoreapi.entities.Product
import org.example.onlinestoreapi.repositories.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService(
    @Autowired
    private val repository: ProductRepository
) {

    fun getAllProducts(): List<Product>  {
        return repository.findAll()
    }

    fun addProduct(product: Product): Product {
        return repository.save(product)
    }

    fun getProductById(id: UUID): Product? {
        return repository.findById(id).orElse(null)
    }

    fun deleteProductById(id: UUID) {
        return repository.deleteById(id)
    }
}
