package com.team5.jakarta.model;

import java.util.Objects;

public class Category {

    private int id;
    private String name;
    private String description;
    private Integer parentId;

    public Category() {}

    public Category(int id, String name, String description, Integer parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public boolean isRoot() { return parentId == null; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return id == category.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "', parentId=" + parentId + "}";
    }
}

