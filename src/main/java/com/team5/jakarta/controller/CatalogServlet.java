package com.team5.jakarta.controller;

import com.team5.jakarta.data.DataStore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "catalogServlet", urlPatterns = {"/catalog", ""})
public class CatalogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataStore store = DataStore.getInstance();
        request.setAttribute("rootCategories", store.getRootCategories());
        request.setAttribute("totalProducts", store.getProducts().size());
        request.setAttribute("totalCategories", store.getCategories().size());
        request.getRequestDispatcher("/WEB-INF/views/catalog.jsp").forward(request, response);
    }
}

