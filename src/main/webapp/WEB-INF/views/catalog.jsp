<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Каталог товарів"/>
<%@ include file="includes/header.jsp" %>

<div class="container">
    <h1>Каталог товарів</h1>
    <p style="color:#555;margin-bottom:20px;">
        Оберіть категорію, щоб переглянути товари.
        Всього у каталозі: <strong><c:out value="${totalCategories}"/></strong> категорій
        та <strong><c:out value="${totalProducts}"/></strong> товарів.
    </p>

    <c:choose>
        <c:when test="${empty rootCategories}">
            <p class="empty-msg">Каталог порожній. Зверніться до адміністратора.</p>
        </c:when>
        <c:otherwise>
            <div class="card-grid">
                <c:forEach var="cat" items="${rootCategories}">
                    <a class="card" href="${pageContext.request.contextPath}/category?id=<c:out value='${cat.id}'/>">
                        <div class="card-title"><c:out value="${cat.name}"/></div>
                        <c:if test="${not empty cat.description}">
                            <div class="card-desc"><c:out value="${cat.description}"/></div>
                        </c:if>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="includes/footer.jsp" %>

