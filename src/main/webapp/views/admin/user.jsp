<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <jsp:include page="../layout/admin-header.jsp" />

    <div class="container pb-5">
        <div class="card mb-4 border-0 shadow-sm">
            <div class="card-header bg-primary text-white fw-bold">USER EDITION</div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/user" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Username</label>
                            <input name="id" class="form-control" value="${user.id}" ${not empty user.id ? 'readonly' : ''} required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Password</label>
                            <input name="password" type="password" class="form-control" autocomplete="new-password" placeholder="Enter a new password (leave blank when editing)">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Fullname</label>
                            <input name="fullname" class="form-control" value="${user.fullname}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold small">Email Address</label>
                            <input name="email" type="email" class="form-control" value="${user.email}" required>
                        </div>
                        <!-- Loại bỏ radio button Admin: Mọi user tạo mới từ đây mặc định là User thường -->
                        <input type="hidden" name="admin" value="false">
                    </div>
                    <div class="border-top pt-3">
                        <button name="action" value="create" class="btn btn-success px-4" ${not empty user.id ? 'disabled' : ''}>Create</button>
                        <button name="action" value="update" class="btn btn-primary px-4" ${empty user.id ? 'disabled' : ''}>Update</button>
                        <button name="action" value="delete" class="btn btn-danger px-4" ${empty user.id ? 'disabled' : ''}>Delete</button>
                        <a href="${pageContext.request.contextPath}/admin/user" class="btn btn-secondary px-4">Reset</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-header bg-dark text-white fw-bold">USER LIST</div>
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="table-light"><tr><th>Username</th><th>Password</th><th>Fullname</th><th>Email</th><th>Role</th><th>Action</th></tr></thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.id}</td>
                                <td>••••••</td>
                                <td>${u.fullname}</td>
                                <td>${u.email}</td>
                                <td><span class="badge ${u.admin ? 'bg-warning text-dark' : 'bg-secondary'}">${u.admin ? 'Admin' : 'User'}</span></td>
                                <td><a href="${pageContext.request.contextPath}/admin/user?id=${u.id}" class="btn btn-sm btn-info py-0 px-2"><i class="bi bi-pencil-square"></i> Edit</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
