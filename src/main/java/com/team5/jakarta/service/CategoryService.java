package com.team5.jakarta.service;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Category;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class CategoryService {

    private final DataStore dataStore = DataStore.getInstance();

    public List<Category> getAllCategories() {
        return dataStore.getCategories();
    }

    public List<Category> getRootCategories() {
        return dataStore.getRootCategories();
    }

    public List<Category> getChildCategories(int parentId) {
        return dataStore.getChildCategories(parentId);
    }

    public Category getCategoryById(int id) {
        return dataStore.getCategoryById(id);
    }

    public List<Category> getCategoryBreadcrumb(int id) {
        return dataStore.getCategoryBreadcrumb(id);
    }

    public Category addCategory(Category category) {
        return dataStore.addCategory(category);
    }

    public boolean updateCategory(Category category) {
        return dataStore.updateCategory(category);
    }

    public boolean deleteCategory(int id) {
        return dataStore.deleteCategory(id);
    }
}
