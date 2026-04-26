package com.team5.jakarta.controller.admin;

import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminDashboardServlet", urlPatterns = "/admin")
public class AdminDashboardServlet extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @EJB
    private ProductService productService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categoryCount", categoryService.getAllCategories().size());
        request.setAttribute("productCount", productService.getAllProducts().size());
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}

