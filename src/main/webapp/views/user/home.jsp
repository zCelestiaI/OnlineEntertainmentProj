<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- CỰC KỲ QUAN TRỌNG: Nhúng thư viện icon để hiện nút Toggle Theme -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .video-card { border: none; border-radius: 12px; overflow: hidden; transition: transform 0.3s; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        .video-card:hover { transform: translateY(-5px); }
        .video-poster { height: 180px; object-fit: cover; }
        .video-title { text-decoration: none; display: block; font-weight: bold; color: inherit; }
        .card-footer { border-top: 1px solid rgba(0,0,0,0.05); }
    </style>
</head>
<body>
    <jsp:include page="../layout/menu.jsp" />

    <div class="container py-4">
        <c:if test="${not empty currentUser}">
            <div class="d-flex justify-content-end mb-3">
                <a href="${pageContext.request.contextPath}/chat" class="btn btn-warning fw-semibold">
                    <i class="bi bi-chat-dots-fill me-1"></i>Community Chat
                </a>
            </div>
        </c:if>

        <div class="row g-4">
            <c:forEach var="v" items="${videos}">
                <div class="col-lg-4 col-md-6">
                    <div class="card video-card h-100">
                        <a href="${pageContext.request.contextPath}/video?id=${v.id}">
                            <img src="${v.poster}" class="card-img-top video-poster" alt="${v.title}">
                        </a>
                        <div class="card-body">
                            <a href="${pageContext.request.contextPath}/video?id=${v.id}" class="video-title text-truncate">
                                ${v.title}
                            </a>
                            <p class="text-muted small mt-2 text-truncate">${v.description}</p>
                        </div>
                        <div class="card-footer d-flex justify-content-between align-items-center">
                            <span class="small text-muted">${v.views} views</span>
                            <div class="btn-group">
                                <a href="${pageContext.request.contextPath}/favorite?action=like&id=${v.id}" class="btn btn-sm btn-outline-danger">Like</a>
                                <a href="${pageContext.request.contextPath}/share?videoId=${v.id}" class="btn btn-sm btn-outline-primary ms-1">Share</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav class="mt-5">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a class="page-link" href="?page=1">First</a></li>
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage - 1}">Previous</a></li>
                    <li class="page-item active"><span class="page-link">${currentPage} / ${totalPages}</span></li>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage + 1}">Next</a></li>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}"><a class="page-link" href="?page=${totalPages}">Last</a></li>
                </ul>
            </nav>
        </c:if>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
