<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${category.name}"/>
<%@ include file="includes/header.jsp" %>

<div class="container">

    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/catalog">Каталог</a>
        <c:forEach var="crumb" items="${breadcrumb}">
            <span>›</span>
            <c:choose>
                <c:when test="${crumb.id == category.id}">
                    <strong><c:out value="${crumb.name}"/></strong>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/category?id=<c:out value='${crumb.id}'/>">
                        <c:out value="${crumb.name}"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>

    <h1><c:out value="${category.name}"/></h1>
    <c:if test="${not empty category.description}">
        <p style="color:#555;margin-bottom:20px;"><c:out value="${category.description}"/></p>
    </c:if>

    <c:if test="${not empty childCategories}">
        <h2>Підкатегорії</h2>
        <div class="card-grid">
            <c:forEach var="sub" items="${childCategories}">
                <a class="card" href="${pageContext.request.contextPath}/category?id=<c:out value='${sub.id}'/>">
                    <div class="card-title"><c:out value="${sub.name}"/></div>
                    <c:if test="${not empty sub.description}">
                        <div class="card-desc"><c:out value="${sub.description}"/></div>
                    </c:if>
                </a>
            </c:forEach>
        </div>
    </c:if>

    <h2>Товари</h2>
    <c:choose>
        <c:when test="${empty products}">
            <p class="empty-msg">У цій категорії немає товарів.</p>
        </c:when>
        <c:otherwise>
            <div class="card-grid">
                <c:forEach var="prod" items="${products}">
                    <a class="card" href="${pageContext.request.contextPath}/product?id=<c:out value='${prod.id}'/>">
                        <div class="card-title"><c:out value="${prod.name}"/></div>
                        <div class="card-desc"><c:out value="${prod.description}"/></div>
                        <div class="card-price"><c:out value="${prod.price}"/> ₴</div>
                        <c:if test="${prod.available}">
                            <span class="badge badge-success" style="margin-top:8px;">В наявності</span>
                        </c:if>
                        <c:if test="${!prod.available}">
                            <span class="badge badge-danger" style="margin-top:8px;">Немає в наявності</span>
                        </c:if>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>

<%@ include file="includes/footer.jsp" %>

