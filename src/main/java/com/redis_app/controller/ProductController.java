package com.redis_app.controller;

import com.redis_app.entity.Product;
import com.redis_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;


    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return "Deleted Successfully";
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }


//    @GetMapping("/{id}")
//    public Product getProductById(@PathVariable Long id){
//        return productService.getProductById(id);
//    }
//
//    @PostMapping
//     public Product saveProduct(@RequestBody Product product){
//        return productService.saveProduct(product);
//    }
//
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        productService.deleteProduct(id);
//    }
}
