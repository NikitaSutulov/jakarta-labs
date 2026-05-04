package com.team5.jakarta.controller.admin;

import com.team5.jakarta.model.Category;
import com.team5.jakarta.service.CategoryService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "adminCategoryServlet", urlPatterns = "/admin/category")
public class AdminCategoryServlet extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "new" -> {
                request.setAttribute("allCategories", categoryService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/views/admin/category-form.jsp").forward(request, response);
            }
            case "edit" -> {
                int id = parseId(request.getParameter("id"));
                Category category = categoryService.getCategoryById(id);
                if (category == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                request.setAttribute("category", category);
                request.setAttribute("allCategories", categoryService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/views/admin/category-form.jsp").forward(request, response);
            }
            case "findByName" -> {
                String name = request.getParameter("name");
                if (!name.trim().isEmpty()) {
                    request.setAttribute("categories", categoryService.getCategoriesByNameContaining(name));
                    request.getRequestDispatcher("/WEB-INF/views/admin/category-list.jsp").forward(request, response);
                } else {
                    request.setAttribute("categories", categoryService.getAllCategories());
                    request.getRequestDispatcher("/WEB-INF/views/admin/category-list.jsp").forward(request, response);
                }
            }
            case null, default -> {
                request.setAttribute("categories", categoryService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/views/admin/category-list.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            Category c = new Category();
            c.setName(request.getParameter("name"));
            c.setDescription(request.getParameter("description"));
            String parentIdStr = request.getParameter("parentId");
            c.setParent((parentIdStr == null || parentIdStr.isBlank()) ? null : categoryService.getCategoryById(Integer.parseInt(parentIdStr)));
            categoryService.addCategory(c);

        } else if ("update".equals(action)) {
            int id = parseId(request.getParameter("id"));
            Category c = categoryService.getCategoryById(id);
            if (c != null) {
                c.setName(request.getParameter("name"));
                c.setDescription(request.getParameter("description"));
                String parentIdStr = request.getParameter("parentId");
                c.setParent((parentIdStr == null || parentIdStr.isBlank()) ? null : categoryService.getCategoryById(Integer.parseInt(parentIdStr)));
                categoryService.updateCategory(c);
            }

        } else if ("delete".equals(action)) {
            int id = parseId(request.getParameter("id"));
            categoryService.deleteCategory(id);
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

