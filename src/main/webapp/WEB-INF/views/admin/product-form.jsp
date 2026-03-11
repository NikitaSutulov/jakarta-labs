<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:choose>
    <c:when test="${product != null}">
        <c:set var="pageTitle" value="Редагувати товар"/>
        <c:set var="formAction" value="update"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="Новий товар"/>
        <c:set var="formAction" value="create"/>
    </c:otherwise>
</c:choose>
<%@ include file="../includes/header.jsp" %>

<div class="container">
    <div style="display:flex;align-items:center;gap:16px;margin-bottom:20px;">
        <h1><c:out value="${pageTitle}"/></h1>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/product">← Назад до списку</a>
    </div>

    <div class="form-card">
        <form method="post" action="${pageContext.request.contextPath}/admin/product">
            <input type="hidden" name="action" value="<c:out value='${formAction}'/>"/>
            <c:if test="${product != null}">
                <input type="hidden" name="id" value="<c:out value='${product.id}'/>"/>
            </c:if>

            <div class="form-group">
                <label for="name">Назва товару *</label>
                <input type="text" id="name" name="name" required
                       value="<c:out value='${product.name}'/>"/>
            </div>

            <div class="form-group">
                <label for="description">Опис</label>
                <textarea id="description" name="description"><c:out value="${product.description}"/></textarea>
            </div>

            <div class="form-group">
                <label for="price">Ціна (₴) *</label>
                <input type="number" id="price" name="price" step="0.01" min="0" required
                       value="<c:out value='${product.price}'/>"/>
            </div>

            <div class="form-group">
                <label for="imageUrl">URL зображення</label>
                <input type="url" id="imageUrl" name="imageUrl"
                       value="<c:out value='${product.imageUrl}'/>"/>
            </div>

            <div class="form-group">
                <label for="categoryId">Категорія *</label>
                <select id="categoryId" name="categoryId" required>
                    <option value="">— Оберіть категорію —</option>
                    <c:forEach var="cat" items="${allCategories}">
                        <option value="<c:out value='${cat.id}'/>"
                                <c:if test="${product != null && product.categoryId == cat.id}">selected</c:if>>
                            <c:out value="${cat.name}"/>
                            <c:if test="${cat.parentId == null}"> [корінь]</c:if>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox" name="available"
                           <c:if test="${product == null || product.available}">checked</c:if>/>
                    В наявності
                </label>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">
                    <c:choose>
                        <c:when test="${product != null}">Зберегти зміни</c:when>
                        <c:otherwise>Створити товар</c:otherwise>
                    </c:choose>
                </button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/product">Скасувати</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>

