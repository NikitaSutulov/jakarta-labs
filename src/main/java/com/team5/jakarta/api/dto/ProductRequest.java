package com.team5.jakarta.api.dto;

import com.team5.jakarta.api.validation.ValidPriceRange;
import com.team5.jakarta.api.validation.ValidSku;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidPriceRange
public class ProductRequest {

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 120, message = "name length must be between 3 and 120")
    @ValidSku
    private String name;

    @NotBlank(message = "description is required")
    @Size(min = 5, max = 500, message = "description length must be between 5 and 500")
    private String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price must be >= 0")
    private Double price;

    @Size(max = 300, message = "imageUrl length must be <= 300")
    private String imageUrl;

    @NotNull(message = "categoryId is required")
    private Integer categoryId;

    @NotNull(message = "available is required")
    private Boolean available;

    @DecimalMin(value = "0.0", inclusive = true, message = "discountPrice must be >= 0")
    private Double discountPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }
}

