package com.team5.jakarta.controller;

import com.team5.jakarta.model.Category;
import com.team5.jakarta.model.Product;
import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "productServlet", urlPatterns = "/product")
public class ProductServlet extends HttpServlet {

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product id");
            return;
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        Category category = categoryService.getCategoryById(product.getCategoryId());
        request.setAttribute("product", product);
        request.setAttribute("category", category);
        if (category != null) {
            request.setAttribute("breadcrumb", categoryService.getCategoryBreadcrumb(category.getId()));
        }
        request.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(request, response);
    }
}
