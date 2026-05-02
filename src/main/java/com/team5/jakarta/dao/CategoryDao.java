package com.team5.jakarta.dao;

import com.team5.jakarta.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryDao implements Dao<Category> {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final CategoryDao INSTANCE = new CategoryDao();

    private final static String UPDATE_SQL = """
            UPDATE categories SET name = ?, description = ?, parent_id = ? WHERE id = ?
            """;

    private final static String SAVE_SQL = """
            INSERT INTO categories (name, description, parent_id) VALUES (?, ?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM categories WHERE id = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT id, name, description, parent_id FROM categories
            """;

    private final static String FIND_ALL_ROOT_SQL = """
            SELECT id, name, description, parent_id FROM categories WHERE parent_id IS null
            """;

    private final static String FIND_ALL_CHILD_SQL = """
            SELECT id, name, description, parent_id FROM categories WHERE parent_id = ?
            """;

    private final static String FIND_ALL_BY_NAME_CONTAINING_SQL = """
            SELECT id, name, description, parent_id FROM categories WHERE name ILIKE ?
            """;

    private final static String FIND_BY_ID_SQL = """
            SELECT id, name, description, parent_id FROM categories WHERE id = ?
            """;

    private final static String COUNT_ALL_SQL = """
            SELECT COUNT(*) FROM categories
            """;

    private CategoryDao() {
    }

    public static CategoryDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Category> findAll() {
        logger.debug("Trying find all categories");
        return findAll(FIND_ALL_SQL);
    }

    @Override
    public Optional<Category> findById(int id) {
        logger.debug("Trying find category by ID {}", id);
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_ID_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Category(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getObject(4, Integer.class)
                        )
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Category> findAllRootCategories() {
        logger.debug("Trying find all root categories");
        return findAll(FIND_ALL_ROOT_SQL);
    }

    public List<Category> findAllChildCategoriesById(int parentId) {
        logger.debug("Trying find all child categories of parent id {}", parentId);
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL_CHILD_SQL)) {
            statement.setInt(1, parentId);
            ResultSet rs = statement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getObject(4, Integer.class)
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Category> findAll(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getObject(4, Integer.class)
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category save(Category category) {
        logger.debug("Trying safe category: {}", category);
        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setObject(3, category.getParentId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Помилка: ID не було згенеровано.");
                }
            }
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        logger.debug("Trying delete category with ID: {}", id);
        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(DELETE_SQL)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Category category) {
        return delete(category.getId());
    }

    @Override
    public boolean update(Category category) {
        logger.debug("Trying update category with ID: {}", category.getId());
        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setObject(3, category.getParentId());
            statement.setInt(4, category.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Category with ID {} successful UPDATE", category.getId());
                return true;
            } else {
                logger.warn("Category with ID {} is not found for UPDATE ", category.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Exception when UPDATE for category {}", category.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAll() {
        logger.debug("Trying count all categories");
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Exception when counting all categories", e);
        }
        return 0;
    }

    public List<Category> findByNameContaining(String name) {
        logger.debug("Trying find all categories, which name contains: {}", name);
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL_BY_NAME_CONTAINING_SQL)) {
            statement.setString(1, "%" + name + "%");
            ResultSet rs = statement.executeQuery();
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getObject(4, Integer.class)
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
