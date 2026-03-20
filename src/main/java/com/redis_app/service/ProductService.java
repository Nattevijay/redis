package com.redis_app.service;

import com.redis_app.entity.Product;
import com.redis_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY = "product:";


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 🔥 GET (Cache Aside Pattern)
    public Product getProductById(Long id) {

        String key = KEY + id;

        // 1. Check Redis
        Product product = (Product) redisTemplate.opsForValue().get(key);

        if (product != null) {
            System.out.println("✅ Cache HIT (Redis)");
            return product;
        }

        // 2. Fetch from DB
        System.out.println("❌ Cache MISS → Fetching from DB");
        product = productRepository.findById(id).orElse(null);

        if (product != null) {
            // 3. Store in Redis (TTL: 5 min)
            redisTemplate.opsForValue().set(key, product, 5, TimeUnit.MINUTES);
        }

        return product;
    }

    // 🔥 CREATE
    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);

        // Save in cache
        String key = KEY + saved.getId();
        redisTemplate.opsForValue().set(key, saved, 5, TimeUnit.MINUTES);

        return saved;
    }

    // 🔥 UPDATE
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existing = productRepository.findById(id).orElseThrow();

        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());

        Product saved = productRepository.save(existing);

        // Update cache
        String key = KEY + id;
        redisTemplate.opsForValue().set(key, saved, 5, TimeUnit.MINUTES);

        return saved;
    }

    // 🔥 DELETE
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);

        // Remove from cache
        String key = KEY + id;
        redisTemplate.delete(key);
    }

//    // CACHEABLE → stores result in Redis
//    @Cacheable(value = "products", key = "#id")
//    public Product getProductById(Long id) {
//        System.out.println("Fetching product DB: " + id);
//        return productRepository.findById(id).orElse(null);
//    }
//
////    @CacheEvict(value = "products", key = "#product.id")
//    public Product saveProduct(Product product){
//        System.out.println("product removed: " + product.getId());
//        return productRepository.save(product);
//    }
//
//    @CacheEvict(value = "products", key = "#id")
//    public void deleteProduct(Long id){
//        productRepository.deleteById(id);
//    }

}
