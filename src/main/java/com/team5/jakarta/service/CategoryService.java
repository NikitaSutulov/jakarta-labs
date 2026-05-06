package com.team5.jakarta.service;

import com.team5.jakarta.dao.CategoryDao;
import com.team5.jakarta.model.Category;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CategoryService {

    @Inject
    private CategoryDao categoryDao;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getRootCategories() {
        return categoryDao.findAllRootCategories();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getChildCategories(int parentId) {
        return categoryDao.findAllChildCategoriesById(parentId);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Category getCategoryById(int id) {
        return categoryDao.findById(id).orElse(null);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getCategoryBreadcrumb(int id) {
        List<Category> breadcrumb = new ArrayList<>();
        Category current = getCategoryById(id);
        while (current != null) {
            breadcrumb.addFirst(current);
            if (current.getParent() == null) break;
            current = current.getParent();
        }
        return breadcrumb;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addCategory(Category category) {
        categoryDao.save(category);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean updateCategory(Category category) {
        return categoryDao.update(category);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteCategory(int id) {
        return categoryDao.delete(id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getCategoriesByNameContaining(String name) {
        return categoryDao.findByNameContaining(name);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int countAll() {
        return categoryDao.countAll();
    }
}
