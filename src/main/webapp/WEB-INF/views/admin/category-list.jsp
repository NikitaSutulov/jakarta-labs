<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Категорії — Адмін"/>
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px;">
        <h1>Категорії</h1>
        <div class="actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/category?action=new">+ Додати
                категорію</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin">← Адмін-панель</a>
        </div>
    </div>

    <div class="search-bar" style="margin-bottom: 20px; background: #f4f4f4; padding: 15px; border-radius: 8px;">
        <form method="get" action="${pageContext.request.contextPath}/admin/category" style="display: flex; gap: 10px;">
            <input type="hidden" name="action" value="findByName">

            <input type="text" name="name"
                   placeholder="Введіть назву категорії..."
                   value="<c:out value='${param.name}'/>"
                   style="flex-grow: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">

            <button type="submit" class="btn btn-primary">Пошук</button>

            <c:if test="${not empty param.name}">
                <a href="${pageContext.request.contextPath}/admin/category" class="btn btn-secondary">Очистити</a>
            </c:if>
        </form>
    </div>

    <c:choose>
        <c:when test="${empty categories}">
            <p class="empty-msg">Категорій ще немає.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Назва</th>
                    <th>Опис</th>
                    <th>Батьківська категорія (ID)</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cat" items="${categories}">
                    <tr>
                        <td><c:out value="${cat.id}"/></td>
                        <td><c:out value="${cat.name}"/></td>
                        <td><c:out value="${cat.description}"/></td>
                        <td>
                            <c:if test="${cat.parent == null}">
                                <span style="color:#888;font-style:italic;">Коренева</span>
                            </c:if>
                            <c:if test="${cat.parent != null}">
                                <c:out value="${cat.parent.id}"/>
                            </c:if>
                        </td>
                        <td>
                            <div class="actions">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/admin/category?action=edit&id=<c:out value='${cat.id}'/>">
                                    Редагувати
                                </a>
                                <form class="inline" method="post"
                                      action="${pageContext.request.contextPath}/admin/category"
                                      onsubmit="return confirm('Видалити категорію та всі вкладені дані?')">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="id" value="<c:out value='${cat.id}'/>"/>
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

