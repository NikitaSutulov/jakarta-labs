package com.team5.jakarta.service;

import com.team5.jakarta.dao.CategoryDao;
import com.team5.jakarta.model.Category;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CategoryService {

    @Inject
    private CategoryDao categoryDao;

    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    public List<Category> getRootCategories() {
        return categoryDao.findAllRootCategories();
    }

    public List<Category> getChildCategories(int parentId) {
        return categoryDao.findAllChildCategoriesById(parentId);
    }

    public Category getCategoryById(int id) {
        return categoryDao.findById(id).orElse(null);
    }

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

    public void addCategory(Category category) {
        categoryDao.save(category);
    }

    public boolean updateCategory(Category category) {
        return categoryDao.update(category);
    }

    public boolean deleteCategory(int id) {
        return categoryDao.delete(id);
    }

    public List<Category> getCategoriesByNameContaining(String name) {
        return categoryDao.findByNameContaining(name);
    }

    public int countAll() {
        return categoryDao.countAll();
    }
}
