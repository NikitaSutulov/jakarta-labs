<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${category != null}">
        <c:set var="pageTitle" value="Редагувати категорію"/>
        <c:set var="formAction" value="update"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="Нова категорія"/>
        <c:set var="formAction" value="create"/>
    </c:otherwise>
</c:choose>
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <div style="display:flex;align-items:center;gap:16px;margin-bottom:20px;">
        <h1><c:out value="${pageTitle}"/></h1>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/category">← Назад до списку</a>
    </div>

    <div class="form-card">
        <form method="post" action="${pageContext.request.contextPath}/admin/category">
            <input type="hidden" name="action" value="<c:out value='${formAction}'/>"/>
            <c:if test="${category != null}">
                <input type="hidden" name="id" value="<c:out value='${category.id}'/>"/>
            </c:if>

            <div class="form-group">
                <label for="name">Назва категорії *</label>
                <input type="text" id="name" name="name" required
                       value="<c:out value='${category.name}'/>"/>
            </div>

            <div class="form-group">
                <label for="description">Опис</label>
                <textarea id="description" name="description"><c:out value="${category.description}"/></textarea>
            </div>

            <div class="form-group">
                <label for="parentId">Батьківська категорія</label>
                <select id="parentId" name="parentId">
                    <option value="">— Коренева категорія —</option>
                    <c:forEach var="cat" items="${allCategories}">
                        <c:if test="${category == null || cat.id != category.id}">
                            <option value="<c:out value='${cat.id}'/>"
                                    <c:if test="${category != null && category.parentId == cat.id}">selected</c:if>>
                                <c:out value="${cat.name}"/>
                                <c:if test="${cat.parentId != null}"> (підкатегорія)</c:if>
                            </option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">
                    <c:choose>
                        <c:when test="${category != null}">Зберегти зміни</c:when>
                        <c:otherwise>Створити категорію</c:otherwise>
                    </c:choose>
                </button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/category">Скасувати</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

