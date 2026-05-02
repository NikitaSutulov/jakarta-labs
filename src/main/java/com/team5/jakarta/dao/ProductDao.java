package com.team5.jakarta.dao;

import com.team5.jakarta.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements Dao<Product>{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final ProductDao INSTANCE = new ProductDao();

    private final static String UPDATE_SQL = """
            UPDATE products SET name = ?, description = ?, price = ?, image_url = ?, category_id = ?, available = ?  WHERE id = ?
            """;

    private final static String SAVE_SQL = """
            INSERT INTO products (name, description, price, image_url, category_id, available) VALUES (?, ?, ?, ?, ?, ?)
            """;

    private final static String DELETE_SQL = """
            DELETE FROM products WHERE id = ?
            """;

    private final static String FIND_ALL_SQL = """
            SELECT id, name, description, price, image_url, category_id, available FROM products
            """;

    private final static String FIND_ALL_BY_CATEGORY_SQL = """
            SELECT id, name, description, price, image_url, category_id, available FROM products where category_id = ?
            """;

    private final static String FIND_BY_ID_SQL = """
            SELECT id, name, description, price, image_url, category_id, available FROM products WHERE id = ?
            """;

    private final static String COUNT_ALL_SQL = """
            SELECT COUNT(*) FROM products
            """;

    private ProductDao() {
    }

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> findAll() {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            return getProductsFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findAllByCategoryId(int categoryId) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL_BY_CATEGORY_SQL)) {
            statement.setInt(1, categoryId);
            ResultSet rs = statement.executeQuery();
            return getProductsFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> getProductsFromResultSet(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getBoolean(7)
            ));
        }
        return products;
    }

    @Override
    public Optional<Product> findById(int id) {
        logger.debug("Trying find product by ID {}", id);
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_ID_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Product(
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getDouble(4),
                                rs.getString(5),
                                rs.getInt(6),
                                rs.getBoolean(7)
                        )
                );
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product save(Product product) {
        logger.debug("Trying safe product: {}", product);
        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getImageUrl());
            statement.setInt(5, product.getCategoryId());
            statement.setBoolean(6, product.isAvailable());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Помилка: ID не було згенеровано.");
                }
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        logger.debug("Trying delete product with ID: {}", id);
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
    public boolean delete(Product product) {
        return delete(product.getId());
    }

    @Override
    public boolean update(Product product) {
        logger.debug("Trying update product with ID: {}", product.getId());
        try (Connection connection = getConnection();
             var statement = connection.prepareStatement(UPDATE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setString(5, product.getImageUrl());
            statement.setInt(6, product.getCategoryId());
            statement.setBoolean(7, product.isAvailable());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Product with ID {} successful UPDATE", product.getId());
                return true;
            } else {
                logger.warn("Product with ID {} is not found for UPDATE ", product.getId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Exception when UPDATE for product {}", product.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAll() {
        logger.debug("Trying count all products");
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Exception when counting all products", e);
        }
        return 0;
    }
}
