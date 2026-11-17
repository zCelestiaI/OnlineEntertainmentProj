<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <jsp:include page="../layout/menu.jsp" />

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-4 card p-4 shadow-sm border-0 rounded-4">
                <h3 class="text-center mb-4 fw-bold text-primary">ĐĂNG NHẬP</h3>

                <c:if test="${not empty message}">
                    <div class="alert alert-danger py-2 small">${message}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label small fw-bold">ID hoặc Email:</label>
                        <input name="id" class="form-control" value="${cookie.rememberMe.value}" placeholder="Nhập ID/Email..." required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-bold">Mật khẩu:</label>
                        <input name="password" type="password" class="form-control" placeholder="Nhập mật khẩu..." required>
                    </div>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" name="rememberMe" id="rememberMe" ${not empty cookie.rememberMe.value ? 'checked' : ''}>
                        <label class="form-check-label small" for="rememberMe">
                            Ghi nhớ tài khoản
                        </label>
                    </div>
                    <div class="d-grid gap-2">
                        <button class="btn btn-primary fw-bold py-2 rounded-pill">Đăng nhập</button>
                        <a href="${pageContext.request.contextPath}/forgot-password" class="btn btn-link text-decoration-none small">Quên mật khẩu?</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
