package com.team5.jakarta.api.resource;

import com.team5.jakarta.api.dto.CategoryResponse;
import com.team5.jakarta.model.Category;
import com.team5.jakarta.service.CategoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    public Response getCategories(@QueryParam("parentId") Integer parentId) {
        List<Category> categories = parentId == null
                ? categoryService.getAllCategories()
                : categoryService.getChildCategories(parentId);

        List<CategoryResponse> response = categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") int id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Category not found by id=" + id);
        }
        return Response.ok(toResponse(category)).build();
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setParentId(category.getParent().getId());
        return response;
    }
}
