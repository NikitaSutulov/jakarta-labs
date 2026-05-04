package com.team5.jakarta.dao;

import com.team5.jakarta.model.Category;
import com.team5.jakarta.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class ProductDao implements Dao<Product>{

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Product> findAll() {
        logger.debug("Trying to find all products");
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public List<Product> findAllByCategoryId(int categoryId) {
        logger.debug("Trying to find all products by categoryId: {}", categoryId);
        return em.createQuery("SELECT p FROM Product p WHERE p.category.id = :categoryId", Product.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
    }

    @Override
    public Optional<Product> findById(int id) {
        logger.debug("Trying find product by ID {}", id);
        return Optional.ofNullable(em.find(Product.class, id));
    }

    @Override
    public Product save(Product product) {
        logger.debug("Trying save product: {}", product);
        em.persist(product);
        return product;
    }

    @Override
    public boolean delete(int id) {
        logger.debug("Trying delete product with ID: {}", id);
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Product product) {
        return delete(product.getId());
    }

    @Override
    public boolean update(Product product) {
        logger.debug("Trying update product with ID: {}", product.getId());
        if (product.getId() == 0 || em.find(Product.class, product.getId()) == null) {
            logger.warn("Product with ID {} not found for update", product.getId());
            return false;
        }
        em.merge(product);
        return true;
    }

    @Override
    public int countAll() {
        logger.debug("Trying count all products");
        return em.createQuery("SELECT COUNT(p) FROM Product p", Integer.class)
                .getSingleResult();
    }
}
