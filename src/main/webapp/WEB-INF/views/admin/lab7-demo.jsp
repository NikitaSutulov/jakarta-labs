<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Демо транзакцій (Lab 7)"/>
<%@ include file="../includes/header.jsp" %>

<style>
    .lab7-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 24px; }
    .lab7-form-card { background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 24px; margin-bottom: 24px; }
    .lab7-form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .lab7-flash { padding: 14px 18px; border-radius: 8px; margin-bottom: 20px; font-weight: 500; }
    .lab7-flash.success { background: #e8f5e9; color: #2e7d32; border-left: 4px solid #2e7d32; }
    .lab7-flash.error   { background: #ffebee; color: #c62828; border-left: 4px solid #c62828; }
    .radio-group label, .check-group label { display: inline-flex; align-items: center; gap: 6px; margin-right: 16px; font-weight: 400; }
    .radio-group input, .check-group input { width: auto; }
    .lab7-tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 600; }
    .lab7-tag.ok  { background: #e8f5e9; color: #2e7d32; }
    .lab7-tag.bad { background: #ffebee; color: #c62828; }
    .lab7-help { background: #f0f4ff; border-left: 4px solid #1a237e; padding: 14px 18px; border-radius: 4px; font-size: 0.85rem; color: #444; line-height: 1.5; margin-bottom: 20px; }
    .lab7-help b { color: #1a237e; }
    @media (max-width: 900px) { .lab7-grid, .lab7-form-row { grid-template-columns: 1fr; } }
</style>

<div class="container">
    <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:20px;">
        <h1>Демо транзакцій (Lab 7)</h1>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin">← Адмін-панель</a>
    </div>

    <div class="lab7-help">
        <b>Сценарій:</b> масово застосовуємо знижку до всіх товарів вибраної категорії —
        в одній транзакції оновлюються кілька рядків таблиці <code>products</code>.
        <br/>
        <b>forceFailure</b> — після оновлень кидається виняток, щоб продемонструвати відкат.
        <br/>
        <b>auditMode</b> = <code>REQUIRED</code>: журнал аудиту бере участь у тій же транзакції — відкочується разом.
        <b>auditMode</b> = <code>REQUIRES_NEW</code>: запис «спроба» зберігається у незалежній транзакції — залишається в БД навіть при відкаті основної.
    </div>

    <c:if test="${not empty flash}">
        <div class="lab7-flash ${flashType}"><c:out value="${flash}"/></div>
    </c:if>

    <div class="lab7-form-card">
        <h2 style="margin-top:0;">Параметри запуску</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/lab7-demo">
            <div class="lab7-form-row">
                <div class="form-group">
                    <label>Категорія (тільки з прямими товарами)</label>
                    <select name="categoryId" required>
                        <c:forEach var="cat" items="${categories}">
                            <c:set var="cnt" value="${productsPerCategory[cat.id]}"/>
                            <c:if test="${cnt != null && cnt > 0}">
                                <option value="${cat.id}">#<c:out value="${cat.id}"/> <c:out value="${cat.name}"/> (<c:out value="${cnt}"/> тов.)</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Знижка, %</label>
                    <input type="number" name="percent" min="1" max="99" step="0.01" value="10" required/>
                </div>
            </div>

            <div class="form-group radio-group">
                <label><b>Режим аудиту (Transaction propagation):</b></label><br/>
                <label><input type="radio" name="auditMode" value="REQUIRED" checked/> REQUIRED (журнал у тій самій транзакції)</label>
                <label><input type="radio" name="auditMode" value="REQUIRES_NEW"/> REQUIRES_NEW (журнал у незалежній транзакції)</label>
            </div>

            <div class="form-group check-group">
                <label><input type="checkbox" name="forceFailure"/> Симулювати помилку після оновлень (для демонстрації відкату)</label>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit">Запустити bulk-discount</button>
            </div>
        </form>
    </div>

    <div class="lab7-grid">
        <div>
            <h2>Поточні ціни товарів</h2>
            <c:choose>
                <c:when test="${empty products}">
                    <p class="empty-msg">Товарів немає.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Назва</th>
                            <th>Категорія</th>
                            <th>Ціна, ₴</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="prod" items="${products}">
                            <c:set var="catName" value="—"/>
                            <c:set var="catId" value="—"/>
                            <c:forEach var="cat" items="${categories}">
                                <c:if test="${cat.id == prod.category.id}">
                                    <c:set var="catName" value="${cat.name}"/>
                                    <c:set var="catId" value="${cat.id}"/>
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td><c:out value="${prod.id}"/></td>
                                <td><c:out value="${prod.name}"/></td>
                                <td><c:out value="#${catId} ${catName}"/></td>
                                <td><c:out value="${prod.price}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div>
            <h2>Журнал аудиту (audit_log)</h2>
            <c:choose>
                <c:when test="${empty auditLogs}">
                    <p class="empty-msg">Журнал порожній. Запустіть будь-який сценарій.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Операція</th>
                            <th>Деталі</th>
                            <th>Час</th>
                            <th>OK?</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="log" items="${auditLogs}">
                            <tr>
                                <td><c:out value="${log.id}"/></td>
                                <td><b><c:out value="${log.operation}"/></b></td>
                                <td><c:out value="${log.details}"/></td>
                                <td>
                                    <c:if test="${log.createdAt != null}">
                                        <c:out value="${log.createdAt}"/>
                                    </c:if>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${log.succeeded}">
                                            <span class="lab7-tag ok">OK</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="lab7-tag bad">FAIL</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<%@ include file="../includes/footer.jsp" %>
