<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Favorites - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .video-card { border: none; border-radius: 15px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1); position: relative; }
        .video-poster { height: 160px; object-fit: cover; }
        .video-title { text-decoration: none; display: block; font-weight: bold; }

        /* Hiệu ứng cho video Inactive */
        .video-card.inactive { opacity: 0.6; filter: grayscale(100%); pointer-events: none; }
        .video-card.inactive .btn-unlike { pointer-events: auto; } /* Vẫn cho phép nhấn Unlike */
        .inactive-overlay {
            position: absolute; top: 0; left: 0; width: 100%; height: 160px;
            background: rgba(0,0,0,0.5); color: white; display: flex;
            align-items: center; justify-content: center; font-weight: bold; z-index: 10;
        }
    </style>
</head>
<body>
    <jsp:include page="../layout/menu.jsp" />

    <div class="container py-4">
        <h2 class="fw-bold text-warning mb-4">MY FAVORITES</h2>

        <div class="row g-4">
            <c:forEach var="f" items="${favorites}">
                <div class="col-lg-3 col-md-4">
                    <div class="card video-card h-100 shadow-sm ${not f.video.active ? 'inactive' : ''}">
                        <c:if test="${not f.video.active}">
                            <div class="inactive-overlay">UNAVAILABLE</div>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/video?id=${f.video.id}">
                            <img src="${f.video.poster}" class="card-img-top video-poster" alt="${f.video.title}">
                        </a>
                        <div class="card-body">
                            <a href="${pageContext.request.contextPath}/video?id=${f.video.id}" class="video-title text-truncate text-dark">
                                ${f.video.title}
                            </a>
                            <div class="mt-3">
                                <a href="${pageContext.request.contextPath}/favorite?action=unlike&id=${f.video.id}"
                                   class="btn btn-sm btn-outline-danger w-100 rounded-pill btn-unlike">
                                   Unlike
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty favorites}">
                <div class="col-12 text-center py-5">
                    <div class="alert alert-light border text-dark">
                        <p class="mb-0 fs-5">Bạn chưa có video yêu thích nào.</p>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-warning mt-3">Khám phá ngay</a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
