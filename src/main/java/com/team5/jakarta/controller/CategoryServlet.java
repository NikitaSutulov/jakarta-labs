package com.team5.jakarta.controller;

import com.team5.jakarta.model.Category;
import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "categoryServlet", urlPatterns = "/category")
public class CategoryServlet extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @EJB
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/catalog");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category id");
            return;
        }

        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
            return;
        }

        request.setAttribute("category", category);
        request.setAttribute("breadcrumb", categoryService.getCategoryBreadcrumb(id));
        request.setAttribute("childCategories", categoryService.getChildCategories(id));
        request.setAttribute("products", productService.getProductsByCategoryId(id));
        request.getRequestDispatcher("/WEB-INF/views/category.jsp").forward(request, response);
    }
}
