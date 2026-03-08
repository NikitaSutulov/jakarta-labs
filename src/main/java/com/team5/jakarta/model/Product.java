package com.team5.jakarta.model;

import java.util.Objects;

public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int categoryId;
    private boolean available;

    public Product() {}

    public Product(int id, String name, String description, double price,
                   String imageUrl, int categoryId, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id == product.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", categoryId=" + categoryId + "}";
    }
}

