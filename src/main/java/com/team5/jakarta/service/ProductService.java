package com.team5.jakarta.service;

import com.team5.jakarta.dao.ProductDao;
import com.team5.jakarta.model.Product;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ProductService {

    @Inject
    private ProductDao productDao;

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        return productDao.findAllByCategoryId(categoryId);
    }

    public Product getProductById(int id) {
        return productDao.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        return productDao.save(product);
    }

    public boolean updateProduct(Product product) {
        return productDao.update(product);
    }

    public boolean deleteProduct(int id) {
        return productDao.delete(id);
    }

    public int countAll() {
        return productDao.countAll();
    }
}
