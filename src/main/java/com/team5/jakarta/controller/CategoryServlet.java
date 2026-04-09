package com.team5.jakarta.controller;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "categoryServlet", urlPatterns = "/category")
public class CategoryServlet extends HttpServlet {

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category id");
            return;
        }

        Category category = store.getCategoryById(id);
        if (category == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
            return;
        }

        request.setAttribute("category", category);
        request.setAttribute("breadcrumb", store.getCategoryBreadcrumb(id));
        request.setAttribute("childCategories", store.getChildCategories(id));
        request.setAttribute("products", store.getProductsByCategoryId(id));
        request.getRequestDispatcher("/WEB-INF/views/category.jsp").forward(request, response);
    }
}

