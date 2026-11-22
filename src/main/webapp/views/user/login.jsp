<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Đăng nhập - Online Entertainment</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head><body class="bg-light"><jsp:include page="../layout/menu.jsp"/>
<main class="container py-5"><div class="row justify-content-center"><div class="col-md-5 col-lg-4">
<div class="card border-0 shadow-sm rounded-4 p-4"><h1 class="h3 fw-bold mb-4">Đăng nhập</h1>
<c:if test="${param.registered eq '1'}"><div class="alert alert-success">Đăng ký thành công. Hãy đăng nhập để quản lý kênh.</div></c:if>
<c:if test="${not empty message}"><div class="alert alert-danger"><c:out value="${message}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
<label class="form-label">Tên đăng nhập hoặc email</label>
<input class="form-control mb-3" name="id" autocomplete="username" placeholder="Username / email" value="<c:out value='${cookie.rememberMe.value}'/>" required>
<label class="form-label">Mật khẩu</label><input class="form-control mb-3" type="password" name="password" autocomplete="current-password" required>
<label class="form-check mb-3"><input class="form-check-input" type="checkbox" name="rememberMe" ${not empty cookie.rememberMe.value ? 'checked' : ''}>
<span class="form-check-label">Ghi nhớ tên đăng nhập trên thiết bị này</span></label>
<button class="btn btn-primary w-100 py-2" type="submit">Đăng nhập</button>
<div class="text-center mt-3"><a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a></div>
<p class="small text-muted text-center mt-2">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</form></div></div></div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</body></html>