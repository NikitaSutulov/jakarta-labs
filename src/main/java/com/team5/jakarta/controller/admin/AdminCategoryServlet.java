package com.team5.jakarta.controller.admin;

import com.team5.jakarta.data.DataStore;
import com.team5.jakarta.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminCategoryServlet", urlPatterns = "/admin/category")
public class AdminCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DataStore store = DataStore.getInstance();
        String action = request.getParameter("action");

        if ("new".equals(action)) {
            request.setAttribute("allCategories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/category-form.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            int id = parseId(request.getParameter("id"));
            Category category = store.getCategoryById(id);
            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            request.setAttribute("category", category);
            request.setAttribute("allCategories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/category-form.jsp").forward(request, response);

        } else {
            request.setAttribute("categories", store.getCategories());
            request.getRequestDispatcher("/WEB-INF/views/admin/category-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        DataStore store = DataStore.getInstance();
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            Category c = new Category();
            c.setName(request.getParameter("name"));
            c.setDescription(request.getParameter("description"));
            String parentIdStr = request.getParameter("parentId");
            c.setParentId((parentIdStr == null || parentIdStr.isBlank()) ? null : Integer.parseInt(parentIdStr));
            store.addCategory(c);

        } else if ("update".equals(action)) {
            int id = parseId(request.getParameter("id"));
            Category c = store.getCategoryById(id);
            if (c != null) {
                c.setName(request.getParameter("name"));
                c.setDescription(request.getParameter("description"));
                String parentIdStr = request.getParameter("parentId");
                c.setParentId((parentIdStr == null || parentIdStr.isBlank()) ? null : Integer.parseInt(parentIdStr));
                store.updateCategory(c);
            }

        } else if ("delete".equals(action)) {
            int id = parseId(request.getParameter("id"));
            store.deleteCategory(id);
        }

        response.sendRedirect(request.getContextPath() + "/admin/category");
    }

    private int parseId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }
}

