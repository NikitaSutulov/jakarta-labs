<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${product.name}"/>
<%@ include file="includes/header.jsp" %>

<div class="container">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/catalog">Каталог</a>
        <c:forEach var="crumb" items="${breadcrumb}">
            <span>›</span>
            <a href="${pageContext.request.contextPath}/category?id=<c:out value='${crumb.id}'/>">
                <c:out value="${crumb.name}"/>
            </a>
        </c:forEach>
        <span>›</span>
        <strong><c:out value="${product.name}"/></strong>
    </div>

    <div class="product-detail">
        <c:choose>
            <c:when test="${not empty product.imageUrl}">
                <img src="<c:out value='${product.imageUrl}'/>" alt="<c:out value='${product.name}'/>"
                     onerror="this.src='https://via.placeholder.com/320x320?text=No+Image'"/>
            </c:when>
            <c:otherwise>
                <img src="https://via.placeholder.com/320x320?text=No+Image"
                     alt="Зображення відсутнє"/>
            </c:otherwise>
        </c:choose>

        <div class="info">
            <h1><c:out value="${product.name}"/></h1>

            <c:if test="${product.available}">
                <span class="badge badge-success" style="margin:10px 0;display:inline-block;">✓ В наявності</span>
            </c:if>
            <c:if test="${!product.available}">
                <span class="badge badge-danger" style="margin:10px 0;display:inline-block;">✗ Немає в наявності</span>
            </c:if>

            <div class="price"><c:out value="${product.price}"/> ₴</div>

            <p style="color:#555;margin:12px 0;line-height:1.6;">
                <c:out value="${product.description}"/>
            </p>

            <c:if test="${category != null}">
                <p style="font-size:0.85rem;color:#777;">
                    Категорія:
                    <a href="${pageContext.request.contextPath}/category?id=<c:out value='${category.id}'/>">
                        <c:out value="${category.name}"/>
                    </a>
                </p>
            </c:if>

            <div class="actions" style="margin-top:20px;">
                <a class="btn btn-secondary" href="javascript:history.back()">← Назад</a>
            </div>
        </div>
    </div>

</div>

<%@ include file="includes/footer.jsp" %>

