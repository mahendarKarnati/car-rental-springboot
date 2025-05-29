package com.website.samcar.service;

import com.website.samcar.model.Product;
import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void updateProduct(Long id, Product updatedProduct);
    void deleteProduct(Long id);
}
