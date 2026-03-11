<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Електронний каталог товарів'}"/></title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Segoe UI', Arial, sans-serif; background: #f5f6fa; color: #333; }
        nav { background: #1a237e; color: #fff; padding: 0 24px; display: flex; align-items: center; gap: 24px; min-height: 56px; }
        nav a { color: #fff; text-decoration: none; font-weight: 500; padding: 16px 4px; border-bottom: 3px solid transparent; }
        nav a:hover { border-bottom-color: #90caf9; }
        nav .brand { font-size: 1.2rem; font-weight: 700; letter-spacing: 1px; flex: 1; }
        .container { max-width: 1200px; margin: 0 auto; padding: 24px 16px; }
        .breadcrumb { font-size: 0.88rem; color: #666; margin-bottom: 16px; }
        .breadcrumb a { color: #1a237e; text-decoration: none; }
        .breadcrumb a:hover { text-decoration: underline; }
        .breadcrumb span { margin: 0 6px; }
        h1 { font-size: 1.8rem; margin-bottom: 8px; color: #1a237e; }
        h2 { font-size: 1.3rem; margin: 24px 0 12px; color: #283593; }
        .card-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px; margin-bottom: 24px; }
        .card { background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 20px; text-decoration: none; color: inherit; transition: transform .15s, box-shadow .15s; }
        .card:hover { transform: translateY(-3px); box-shadow: 0 6px 18px rgba(0,0,0,.13); }
        .card .card-title { font-size: 1rem; font-weight: 600; margin-bottom: 6px; color: #1a237e; }
        .card .card-desc { font-size: 0.83rem; color: #666; }
        .card .card-price { font-size: 1.1rem; font-weight: 700; color: #c62828; margin-top: 10px; }
        .badge { display: inline-block; padding: 2px 10px; border-radius: 12px; font-size: 0.75rem; font-weight: 600; }
        .badge-success { background: #e8f5e9; color: #2e7d32; }
        .badge-danger  { background: #ffebee; color: #c62828; }
        .btn { display: inline-block; padding: 8px 18px; border-radius: 6px; text-decoration: none; font-size: 0.9rem; cursor: pointer; border: none; font-weight: 500; }
        .btn-primary { background: #1a237e; color: #fff; }
        .btn-primary:hover { background: #283593; }
        .btn-danger { background: #c62828; color: #fff; }
        .btn-danger:hover { background: #b71c1c; }
        .btn-secondary { background: #e3e8f0; color: #333; }
        .btn-secondary:hover { background: #cfd5e3; }
        .empty-msg { color: #999; font-style: italic; padding: 16px 0; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,.08); }
        th { background: #1a237e; color: #fff; padding: 12px 14px; text-align: left; font-size: 0.9rem; }
        td { padding: 10px 14px; border-bottom: 1px solid #eee; font-size: 0.9rem; vertical-align: middle; }
        tr:last-child td { border-bottom: none; }
        tr:hover td { background: #f0f4ff; }
        form.inline { display: inline; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-weight: 600; margin-bottom: 4px; font-size: 0.9rem; }
        .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 8px 12px; border: 1px solid #ccc; border-radius: 6px; font-size: 0.95rem; }
        .form-group textarea { min-height: 80px; resize: vertical; }
        .form-card { background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 28px; max-width: 600px; }
        .actions { display: flex; gap: 10px; margin-top: 8px; }
        .product-detail { display: flex; gap: 32px; background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,.08); padding: 28px; }
        .product-detail img { max-width: 320px; border-radius: 8px; object-fit: contain; }
        .product-detail .info { flex: 1; }
        .product-detail .price { font-size: 2rem; font-weight: 700; color: #c62828; margin: 12px 0; }
        .stat-cards { display: flex; gap: 20px; margin-bottom: 28px; }
        .stat-card { background: #fff; border-radius: 10px; padding: 20px 28px; box-shadow: 0 2px 8px rgba(0,0,0,.08); flex: 1; }
        .stat-card .stat-num { font-size: 2.2rem; font-weight: 700; color: #1a237e; }
        .stat-card .stat-label { color: #666; font-size: 0.9rem; }
        @media (max-width: 600px) {
            .product-detail { flex-direction: column; }
            .product-detail img { max-width: 100%; }
            .stat-cards { flex-direction: column; }
        }
    </style>
</head>
<body>
<nav>
    <span class="brand">🛒 Каталог товарів</span>
    <a href="${pageContext.request.contextPath}/catalog">Головна</a>
    <a href="${pageContext.request.contextPath}/admin">Адмін</a>
</nav>

