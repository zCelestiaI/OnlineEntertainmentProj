<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Change Password - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .btn-change { background-color: #ff8c00; color: white; border: none; }
        .btn-change:hover { background-color: #e67e00; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="../layout/menu.jsp" />

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card border shadow-sm rounded-4">
                    <div class="card-header bg-primary text-white py-3 rounded-top-4">
                        <h5 class="mb-0 fw-bold">CHANGE PASSWORD</h5>
                    </div>
                    <div class="card-body p-4">
                        <c:if test="${not empty message}">
                            <div class="alert alert-info py-2 small">${message}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/change-password" method="post">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">Username</label>
                                    <input class="form-control bg-light" value="${currentUser.id}" readonly>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">Current Password</label>
                                    <input name="oldPassword" type="password" class="form-control" required>
                                </div>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">New Password</label>
                                    <input name="newPassword" type="password" class="form-control" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label fw-bold small">Confirm New Password</label>
                                    <input name="confirmPassword" type="password" class="form-control" required>
                                </div>
                            </div>

                            <div class="text-end border-top pt-3">
                                <button class="btn btn-change px-5 fw-bold rounded-pill">Change</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Fix: Thêm JS để dropdown hoạt động -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
