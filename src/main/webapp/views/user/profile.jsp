<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <jsp:include page="../layout/menu.jsp" />

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card shadow border-0 rounded-4">
                    <div class="card-header bg-dark text-white py-3 rounded-top-4">
                        <h5 class="mb-0 text-center text-uppercase fw-bold">User Information</h5>
                    </div>
                    <div class="card-body p-4">
                        <c:if test="${not empty message}">
                            <div class="alert alert-success py-2 text-center small">${message}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Username (ID)</label>
                                <input class="form-control bg-light" value="${user.id}" readonly>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Full Name</label>
                                <input name="fullname" class="form-control" value="${user.fullname}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Email Address</label>
                                <input name="email" type="email" class="form-control" value="${user.email}" required>
                            </div>

                            <!-- Buttons Section -->
                            <div class="d-grid gap-2 mt-4">
                                <button class="btn btn-primary fw-bold py-2 rounded-pill">Update Profile</button>
                                <a href="${pageContext.request.contextPath}/change-password" class="btn btn-outline-warning fw-bold py-2 rounded-pill">
                                    Change Password
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
