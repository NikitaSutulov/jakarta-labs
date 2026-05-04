package com.team5.jakarta.dao;

import com.team5.jakarta.model.Category;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@NoArgsConstructor
public class CategoryDao implements Dao<Category> {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Category> findAll() {
        logger.debug("Trying to find all categories");
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    @Override
    public Optional<Category> findById(int id) {
        logger.debug("Trying find category by ID {}", id);
        return Optional.ofNullable(em.find(Category.class, id));
    }

    public List<Category> findAllRootCategories() {
        logger.debug("Trying to find all root categories");
        return em.createQuery("SELECT c FROM Category c WHERE c.parent IS NULL", Category.class)
                .getResultList();
    }

    public List<Category> findAllChildCategoriesById(int parentId) {
        logger.debug("Trying to find all child categories of parent id {}", parentId);
        return em.createQuery("SELECT c FROM Category c WHERE c.parent.id = :parentId", Category.class)
                .setParameter("parentId", parentId)
                .getResultList();
    }

    @Override
    public Category save(Category category) {
        logger.debug("Trying save category: {}", category);
        em.persist(category);
        return category;
    }

    @Override
    public boolean delete(int id) {
        logger.debug("Trying to delete category with ID: {}", id);
        Category category = em.find(Category.class, id);
        if (category != null) {
            em.remove(category);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Category category) {
        return delete(category.getId());
    }

    @Override
    public boolean update(Category category) {
        logger.debug("Trying to update category with ID: {}", category.getId());
        if (category.getId() == 0 || em.find(Category.class, category.getId()) == null) {
            logger.warn("Category with ID {} not found for update", category.getId());
            return false;
        }
        em.merge(category);
        return true;
    }

    @Override
    public int countAll() {
        logger.debug("Trying to count all categories");
        return em.createQuery("SELECT COUNT(c) FROM Category c", Integer.class)
                .getSingleResult();
    }

    public List<Category> findByNameContaining(String name) {
        logger.debug("Trying to find all categories where name contains: {}", name);
        return em.createQuery("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(:name)", Category.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }
}
