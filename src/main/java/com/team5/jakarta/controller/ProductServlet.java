package com.team5.jakarta.controller;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Category;
import com.team5.jakarta.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "productServlet", urlPatterns = "/product")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataStore store = DataStore.getInstance();

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

        Product product = store.getProductById(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }

        Category category = store.getCategoryById(product.getCategoryId());
        request.setAttribute("product", product);
        request.setAttribute("category", category);
        if (category != null) {
            request.setAttribute("breadcrumb", store.getCategoryBreadcrumb(category.getId()));
        }
        request.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(request, response);
    }
}

