<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Адмін-панель"/>
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <h1>Адмін-панель</h1>
    <p style="color:#555;margin-bottom:24px;">Керування категоріями та товарами каталогу.</p>

    <div class="stat-cards">
        <div class="stat-card">
            <div class="stat-num"><c:out value="${categoryCount}"/></div>
            <div class="stat-label">Категорій</div>
        </div>
        <div class="stat-card">
            <div class="stat-num"><c:out value="${productCount}"/></div>
            <div class="stat-label">Товарів</div>
        </div>
    </div>

    <div class="actions">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/category">Управління категоріями</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/product">Управління товарами</a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalog">← Перейти до каталогу</a>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

