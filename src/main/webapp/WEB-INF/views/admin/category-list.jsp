<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                            <c:if test="${cat.parentId == null}">
                                <span style="color:#888;font-style:italic;">Коренева</span>
                            </c:if>
                            <c:if test="${cat.parentId != null}">
                                <c:out value="${cat.parentId}"/>
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

