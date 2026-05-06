package com.team5.jakarta.controller.admin;

import com.team5.jakarta.model.Product;
import com.team5.jakarta.service.AuditService;
import com.team5.jakarta.service.CategoryService;
import com.team5.jakarta.service.ProductService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "adminLab7DemoServlet", urlPatterns = "/admin/lab7-demo")
public class AdminLab7DemoServlet extends HttpServlet {

    @EJB
    private CategoryService categoryService;

    @EJB
    private ProductService productService;

    @EJB
    private AuditService auditService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> allProducts = productService.getAllProducts();
        Map<Integer, Integer> productsPerCategory = new HashMap<>();
        for (Product p : allProducts) {
            if (p.getCategory() != null) {
                productsPerCategory.merge(p.getCategory().getId(), 1, Integer::sum);
            }
        }
        request.setAttribute("categories", categoryService.getAllCategories());
        request.setAttribute("products", allProducts);
        request.setAttribute("productsPerCategory", productsPerCategory);
        request.setAttribute("auditLogs", auditService.getAll());

        HttpSession session = request.getSession();
        Object flash = session.getAttribute("lab7Flash");
        Object flashType = session.getAttribute("lab7FlashType");
        if (flash != null) {
            request.setAttribute("flash", flash);
            request.setAttribute("flashType", flashType);
            session.removeAttribute("lab7Flash");
            session.removeAttribute("lab7FlashType");
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/lab7-demo.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        int categoryId = parseInt(request.getParameter("categoryId"), -1);
        double percent = parseDouble(request.getParameter("percent"), 0);
        boolean forceFailure = "on".equals(request.getParameter("forceFailure"));
        String auditMode = request.getParameter("auditMode");
        if (auditMode == null || auditMode.isBlank()) {
            auditMode = "REQUIRED";
        }

        String message;
        String type;
        try {
            int updated;
            if ("REQUIRES_NEW".equals(auditMode)) {
                updated = productService.applyDiscountToCategoryWithIndependentAudit(
                        categoryId, percent, forceFailure);
            } else {
                updated = productService.applyDiscountToCategory(
                        categoryId, percent, forceFailure);
            }
            message = "OK: оновлено товарів — " + updated
                    + " (categoryId=" + categoryId + ", percent=" + percent
                    + ", auditMode=" + auditMode + ")";
            type = "success";
        } catch (Throwable t) {
            message = "Помилка (транзакцію відкочено): "
                    + t.getClass().getSimpleName() + " — " + t.getMessage()
                    + " [auditMode=" + auditMode + "]";
            type = "error";
        }

        HttpSession session = request.getSession();
        session.setAttribute("lab7Flash", message);
        session.setAttribute("lab7FlashType", type);

        response.sendRedirect(request.getContextPath() + "/admin/lab7-demo");
    }

    private int parseInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private double parseDouble(String s, double defaultValue) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
