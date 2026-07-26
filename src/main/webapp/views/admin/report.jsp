<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reports & Statistics</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <jsp:include page="../layout/admin-header.jsp" />

    <div class="container pb-5">
        <h2 class="mb-4 fw-bold text-dark">REPORTS & STATISTICS</h2>

        <ul class="nav nav-tabs" id="reportTabs">
            <li class="nav-item"><button class="nav-link active" id="fav-tab" data-bs-toggle="tab" data-bs-target="#favStats">Favorites</button></li>
            <li class="nav-item"><button class="nav-link" id="user-tab" data-bs-toggle="tab" data-bs-target="#favUsers">Favorite Users</button></li>
            <li class="nav-item"><button class="nav-link" id="share-tab" data-bs-toggle="tab" data-bs-target="#sharedFriends">Shared Friends</button></li>
        </ul>

        <div class="tab-content mt-3 bg-white p-4 border border-top-0 shadow-sm rounded-bottom">
            <!-- TAB 1: Favorites -->
            <div class="tab-pane fade show active" id="favStats">
                <table class="table table-hover table-bordered border-light">
                    <thead class="table-dark"><tr><th>Video Title</th><th>Favorite Count</th><th>Latest Date</th><th>Oldest Date</th></tr></thead>
                    <tbody>
                        <c:forEach var="r" items="${favoriteStats}">
                            <tr><td>${r.videoTitle}</td><td><span class="badge bg-primary px-3">${r.favoriteCount}</span></td>
                                <td><fmt:formatDate value="${r.newestDate}" pattern="dd/MM/yyyy"/></td>
                                <td><fmt:formatDate value="${r.oldestDate}" pattern="dd/MM/yyyy"/></td></tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- TAB 2: Favorite Users -->
            <div class="tab-pane fade" id="favUsers">
                <div class="mb-4">
                    <form action="report" method="get" class="row g-3 align-items-center">
                        <div class="col-auto"><label class="fw-bold">Tìm theo Tiêu đề Video:</label></div>
                        <div class="col-md-5">
                            <div class="input-group">
                                <input type="text" name="videoTitle" class="form-control" placeholder="Nhập tên video..." value="${param.videoTitle}">
                                <button class="btn btn-primary"><i class="bi bi-search"></i></button>
                                <a href="report" class="btn btn-outline-secondary">Tất cả</a>
                            </div>
                        </div>
                    </form>
                </div>
                <table class="table table-striped table-bordered">
                    <thead class="table-info"><tr><th>Video Title</th><th>Username</th><th>Fullname</th><th>Email</th><th>Favorite Date</th></tr></thead>
                    <tbody>
                        <c:forEach var="f" items="${favoriteUsers}">
                            <tr><td class="small fw-bold">${f.video.title}</td><td>${f.user.id}</td><td>${f.user.fullname}</td><td>${f.user.email}</td><td><fmt:formatDate value="${f.likeDate}" pattern="dd/MM/yyyy"/></td></tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- TAB 3: Shared Friends -->
            <div class="tab-pane fade" id="sharedFriends">
                <div class="mb-4">
                    <form action="report" method="get" class="row g-3 align-items-center">
                        <div class="col-auto"><label class="fw-bold">Tìm theo Tiêu đề Video:</label></div>
                        <div class="col-md-5">
                            <div class="input-group">
                                <input type="text" name="videoTitleShare" class="form-control" placeholder="Nhập tên video..." value="${param.videoTitleShare}">
                                <button class="btn btn-primary"><i class="bi bi-search"></i></button>
                                <a href="report" class="btn btn-outline-secondary">Tất cả</a>
                            </div>
                        </div>
                    </form>
                </div>
                <table class="table table-striped table-bordered">
                    <thead class="table-success"><tr><th>Video Title</th><th>Người gửi</th><th>Email người gửi</th><th>Email người nhận</th><th>Ngày gửi</th></tr></thead>
                    <tbody>
                        <c:forEach var="s" items="${shareUsers}">
                            <tr><td class="small fw-bold">${s.video.title}</td><td>${s.user.fullname}</td><td>${s.user.email}</td><td>${s.emails}</td><td><fmt:formatDate value="${s.shareDate}" pattern="dd/MM/yyyy"/></td></tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            let activeTab = sessionStorage.getItem('activeTab');
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has('videoTitle')) activeTab = "#user-tab";
            if (urlParams.has('videoTitleShare')) activeTab = "#share-tab";
            if (activeTab) {
                let tabEl = document.querySelector(activeTab);
                if (tabEl) new bootstrap.Tab(tabEl).show();
            }
            document.querySelectorAll('button[data-bs-toggle="tab"]').forEach(tab => {
                tab.addEventListener('shown.bs.tab', function(e) {
                    sessionStorage.setItem('activeTab', '#' + e.target.id);
                });
            });
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
