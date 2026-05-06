package com.team5.jakarta.api.resource;

import com.team5.jakarta.api.dto.PagedResponse;
import com.team5.jakarta.api.dto.ProductRequest;
import com.team5.jakarta.api.dto.ProductResponse;
import com.team5.jakarta.model.Product;
import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    private ProductService productService;

    @Inject
    private CategoryService categoryService;

    @GET
    public Response getProducts(
            @QueryParam("categoryId") Integer categoryId,
            @QueryParam("name") String name,
            @QueryParam("minPrice") @PositiveOrZero Double minPrice,
            @QueryParam("maxPrice") @PositiveOrZero Double maxPrice,
            @DefaultValue("0") @QueryParam("page") @Min(0) int page,
            @DefaultValue("10") @QueryParam("size") @Min(1) @Max(100) int size,
            @DefaultValue("id,asc") @QueryParam("sort") String sort
    ) {
        List<Product> filtered = productService.getAllProducts().stream()
                .filter(p -> categoryId == null || Objects.equals(p.getCategory().getId(), categoryId))
                .filter(p -> name == null || name.isBlank()
                        || p.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new BadRequestException("minPrice must be <= maxPrice");
        }

        filtered.sort(resolveComparator(sort));

        int totalItems = filtered.size();
        int from = Math.min(page * size, totalItems);
        int to = Math.min(from + size, totalItems);
        int totalPages = totalItems == 0 ? 0 : (int) Math.ceil(totalItems / (double) size);

        List<ProductResponse> items = filtered.subList(from, to).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return Response.ok(new PagedResponse<>(items, page, size, totalItems, totalPages)).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found by id=" + id);
        }
        return Response.ok(toResponse(product)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product, @Context UriInfo uriInfo) {
        ensureCategoryExists(product.getCategory().getId());
        Product created = productService.addProduct(product);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(toResponse(created)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") int id, ProductRequest request) {
        Product existing = productService.getProductById(id);
        if (existing == null) {
            throw new NotFoundException("Product not found by id=" + id);
        }

        ensureCategoryExists(request.getCategoryId());

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setImageUrl(request.getImageUrl());
        existing.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        existing.setAvailable(request.getAvailable());

        productService.updateProduct(existing);
        return Response.ok(toResponse(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new NotFoundException("Product not found by id=" + id);
        }
        return Response.noContent().build();
    }

    /**
     * Lab 7 demo endpoint. Масово застосовує знижку до всіх товарів категорії.
     * Демонструє transaction propagation (REQUIRED vs REQUIRES_NEW) і відкат.
     */
    @POST
    @Path("/bulk-discount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response bulkDiscount(
            @QueryParam("categoryId") int categoryId,
            @QueryParam("percent") double percent,
            @DefaultValue("false") @QueryParam("forceFailure") boolean forceFailure,
            @DefaultValue("REQUIRED") @QueryParam("auditMode") String auditMode
    ) {
        int updated = "REQUIRES_NEW".equalsIgnoreCase(auditMode)
                ? productService.applyDiscountToCategoryWithIndependentAudit(categoryId, percent, forceFailure)
                : productService.applyDiscountToCategory(categoryId, percent, forceFailure);

        Map<String, Object> body = new HashMap<>();
        body.put("updated", updated);
        body.put("categoryId", categoryId);
        body.put("percent", percent);
        body.put("auditMode", auditMode);
        return Response.ok(body).build();
    }

    private void ensureCategoryExists(Integer categoryId) {
        if (categoryId == null || categoryService.getCategoryById(categoryId) == null) {
            throw new BadRequestException("Category not found by id=" + categoryId);
        }
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setCategoryId(product.getCategory().getId());
        response.setAvailable(product.getAvailable());
        return response;
    }

    private Comparator<Product> resolveComparator(String sort) {
        String normalized = sort == null ? "id,asc" : sort.toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "price,asc" -> Comparator.comparingDouble(Product::getPrice);
            case "price,desc" -> Comparator.comparingDouble(Product::getPrice).reversed();
            case "name,asc" -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
            case "name,desc" -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER).reversed();
            case "id,desc" -> Comparator.comparingInt(Product::getId).reversed();
            default -> Comparator.comparingInt(Product::getId);
        };
    }
}
