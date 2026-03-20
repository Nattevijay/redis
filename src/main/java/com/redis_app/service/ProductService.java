package com.redis_app.service;

import com.redis_app.entity.Product;
import com.redis_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    // CACHEABLE → stores result in Redis
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("Fetching product DB: " + id);
        return productRepository.findById(id).orElse(null);
    }

//    @CacheEvict(value = "products", key = "#product.id")
    public Product saveProduct(Product product){
        System.out.println("product removed: " + product.getId());
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

}
