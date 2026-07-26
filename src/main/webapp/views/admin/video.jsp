<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Video Management - Assignment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .table thead { background-color: #343a40; color: white; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="../layout/admin-header.jsp" />

    <div class="container py-4">
        <h2 class="fw-bold mb-4">VIDEO MANAGEMENT</h2>
        <div class="row">
            <div class="col-lg-5 mb-4">
                <div class="card shadow-sm border-0">
                    <div class="card-header bg-white"><h5 class="mb-0 fw-bold">Video Edition</h5></div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/video" method="post">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Video ID</label>
                                <input name="id" class="form-control" value="${video.id}" ${not empty video.id ? 'readonly' : ''} required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Title</label>
                                <input name="title" class="form-control" value="${video.title}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Youtube URL</label>
                                <input name="youtubeUrl" class="form-control" placeholder="https://www.youtube.com/watch?v=...">
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Description</label>
                                <textarea name="description" class="form-control" rows="3">${video.description}</textarea>
                            </div>
                            <div class="form-check mb-4">
                                <input class="form-check-input" type="checkbox" name="active" ${video.active or empty video.id ? 'checked' : ''}>
                                <label class="form-check-label">Active</label>
                            </div>
                            <div class="d-flex gap-2 border-top pt-3">
                                <button name="action" value="create" class="btn btn-success flex-fill" ${not empty video.id ? 'disabled' : ''}>Create</button>
                                <button name="action" value="update" class="btn btn-primary flex-fill" ${empty video.id ? 'disabled' : ''}>Update</button>
                                <button name="action" value="delete" class="btn btn-danger flex-fill" ${empty video.id ? 'disabled' : ''}>Delete</button>
                                <a href="${pageContext.request.contextPath}/admin/video" class="btn btn-secondary flex-fill">Reset</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-lg-7">
                <div class="card border-0 shadow-sm">
                    <table class="table table-hover mb-0">
                        <thead><tr><th>Poster</th><th>ID & Title</th><th>Views</th><th>Status</th><th>Action</th></tr></thead>
                        <tbody>
                            <c:forEach var="v" items="${videos}">
                                <tr>
                                    <td><img src="${v.poster}" width="80" class="rounded shadow-sm"></td>
                                    <td><div class="fw-bold">${v.id}</div><div class="small text-muted">${v.title}</div></td>
                                    <td>${v.views}</td>
                                    <td>${v.active ? 'Active' : 'Inactive'}</td>
                                    <td><a href="?id=${v.id}" class="btn btn-sm btn-outline-info">Edit</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
