package com.team5.jakarta.controller.admin;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminProductServlet", urlPatterns = "/admin/product")
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataStore store = DataStore.getInstance();
        String action = request.getParameter("action");

        if ("new".equals(action)) {
            request.setAttribute("allCategories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            int id = parseId(request.getParameter("id"));
            Product product = store.getProductById(id);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("product", product);
            request.setAttribute("allCategories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp").forward(request, response);

        } else {
            request.setAttribute("products", store.getProducts());
            request.setAttribute("allCategories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/product-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        DataStore store = DataStore.getInstance();
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            Product p = new Product();
            p.setName(request.getParameter("name"));
            p.setDescription(request.getParameter("description"));
            p.setPrice(parseDouble(request.getParameter("price")));
            p.setImageUrl(request.getParameter("imageUrl"));
            p.setCategoryId(parseId(request.getParameter("categoryId")));
            p.setAvailable("on".equals(request.getParameter("available")));
            store.addProduct(p);

        } else if ("update".equals(action)) {
            int id = parseId(request.getParameter("id"));
            Product p = store.getProductById(id);
            if (p != null) {
                p.setName(request.getParameter("name"));
                p.setDescription(request.getParameter("description"));
                p.setPrice(parseDouble(request.getParameter("price")));
                p.setImageUrl(request.getParameter("imageUrl"));
                p.setCategoryId(parseId(request.getParameter("categoryId")));
                p.setAvailable("on".equals(request.getParameter("available")));
                store.updateProduct(p);
            }

        } else if ("delete".equals(action)) {
            int id = parseId(request.getParameter("id"));
            store.deleteProduct(id);
        }

        response.sendRedirect(request.getContextPath() + "/admin/product");
    }

    private int parseId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }
}

