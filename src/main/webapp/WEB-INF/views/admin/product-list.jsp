<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Товари — Адмін"/>
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px;">
        <h1>Товари</h1>
        <div class="actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/product?action=new">+ Додати
                товар</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin">← Адмін-панель</a>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty products}">
            <p class="empty-msg">Товарів ще немає.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Назва</th>
                    <th>Ціна, ₴</th>
                    <th>Категорія</th>
                    <th>Наявність</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="prod" items="${products}">
                    <c:set var="catName" value="—"/>
                    <c:forEach var="cat" items="${allCategories}">
                        <c:if test="${cat.id == prod.categoryId}">
                            <c:set var="catName" value="${cat.name}"/>
                        </c:if>
                    </c:forEach>
                    <tr>
                        <td><c:out value="${prod.id}"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/product?id=<c:out value='${prod.id}'/>">
                                <c:out value="${prod.name}"/>
                            </a>
                        </td>
                        <td><c:out value="${prod.price}"/></td>
                        <td><c:out value="${catName}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${prod.available}">
                                    <span class="badge badge-success">В наявності</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-danger">Немає</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="actions">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/admin/product?action=edit&id=<c:out value='${prod.id}'/>">
                                    Редагувати
                                </a>
                                <form class="inline" method="post"
                                      action="${pageContext.request.contextPath}/admin/product"
                                      onsubmit="return confirm('Видалити товар?')">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="<c:out value='${prod.id}'/>"/>
                                    <button class="btn btn-danger" type="submit">Видалити</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="../includes/footer.jsp" %>

