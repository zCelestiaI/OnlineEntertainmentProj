<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Tạo tài khoản - Online Entertainment</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head><body class="bg-light"><jsp:include page="../layout/menu.jsp"/>
<main class="container py-5"><div class="row justify-content-center"><div class="col-md-6 col-lg-5">
<div class="card border-0 shadow-sm rounded-4 p-4"><h1 class="h3 fw-bold mb-2">Tạo tài khoản</h1>
<p class="text-muted">Mỗi tài khoản có một kênh cá nhân để đăng và quản lý video.</p>
<c:if test="${not empty message}"><div class="alert alert-danger"><c:out value="${message}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
<label class="form-label">Tên đăng nhập</label><input class="form-control mb-3" name="id" pattern="[A-Za-z0-9_]{3,20}" maxlength="20" autocomplete="username" required placeholder="3-20 ký tự, chữ/số/_">
<label class="form-label">Họ và tên</label><input class="form-control mb-3" name="fullname" maxlength="50" required>
<label class="form-label">Email</label><input class="form-control mb-3" type="email" name="email" maxlength="50" required>
<label class="form-label">Mật khẩu</label><input class="form-control mb-3" type="password" name="password" minlength="8" autocomplete="new-password" required>
<label class="form-label">Xác nhận mật khẩu</label><input class="form-control mb-4" type="password" name="confirmPassword" minlength="8" autocomplete="new-password" required>
<button class="btn btn-primary w-100 py-2" type="submit">Đăng ký</button>
<p class="text-muted small mt-3 mb-0">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
</form></div></div></div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</body></html>