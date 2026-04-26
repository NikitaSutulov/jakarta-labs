package com.team5.jakarta.service;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Product;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class ProductService {

    private final DataStore dataStore = DataStore.getInstance();

    public List<Product> getAllProducts() {
        return dataStore.getProducts();
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return dataStore.getProductsByCategoryId(categoryId);
    }

    public Product getProductById(int id) {
        return dataStore.getProductById(id);
    }

    public Product addProduct(Product product) {
        return dataStore.addProduct(product);
    }

    public boolean updateProduct(Product product) {
        return dataStore.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return dataStore.deleteProduct(id);
    }
}
