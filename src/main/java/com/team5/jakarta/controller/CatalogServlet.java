package com.team5.jakarta.controller;

import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "catalogServlet", urlPatterns = {"/catalog", ""})
public class CatalogServlet extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @EJB
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("rootCategories", categoryService.getRootCategories());
        request.setAttribute("totalProducts", productService.countAll());
        request.setAttribute("totalCategories", categoryService.countAll());
        request.getRequestDispatcher("/WEB-INF/views/catalog.jsp").forward(request, response);
    }
}

