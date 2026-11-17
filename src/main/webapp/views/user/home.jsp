<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Online Entertainment</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<style>body{background:#f6f8fb}.thumb{height:190px;object-fit:cover}.video-card{border:0;border-radius:12px;overflow:hidden}</style>
</head><body><jsp:include page="../layout/menu.jsp"/><main class="container py-4">
<div class="d-flex justify-content-between align-items-center gap-2 mb-4"><div><h1 class="h3 fw-bold">Khám phá video</h1>
<p class="text-muted mb-0">Nội dung mới từ các kênh</p></div>
<c:if test="${not empty currentUser}"><a class="btn btn-primary" href="${pageContext.request.contextPath}/studio">Đăng video</a></c:if></div>
<c:if test="${empty videos}"><div class="alert alert-secondary">Chưa có video phù hợp.</div></c:if>
<div class="row g-4"><c:forEach var="v" items="${videos}"><div class="col-md-6 col-lg-4">
<div class="card video-card shadow-sm h-100"><a href="${pageContext.request.contextPath}/video?id=${v.id}">
<c:choose><c:when test="${not empty v.poster}"><img class="card-img-top thumb" src="<c:out value='${v.poster}'/>" alt=""></c:when>
<c:otherwise><img class="card-img-top thumb" src="${pageContext.request.contextPath}/assets/img/video-placeholder.svg" alt=""></c:otherwise></c:choose></a>
<div class="card-body"><a class="fw-semibold text-body text-decoration-none" href="${pageContext.request.contextPath}/video?id=${v.id}"><c:out value="${v.title}"/></a>
<div class="small mt-2"><c:choose><c:when test="${not empty v.uploader}"><a href="${pageContext.request.contextPath}/channel?id=${v.uploader.id}"><c:out value="${v.uploaderDisplayName}"/></a></c:when>
<c:otherwise><span class="text-muted">Online Entertainment</span></c:otherwise></c:choose></div>
<div class="text-muted small mt-1">${v.views} lượt xem</div></div></div></div></c:forEach></div>
<c:if test="${totalPages > 1}"><nav class="mt-4"><ul class="pagination justify-content-center">
<li class="page-item ${currentPage <= 1 ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage - 1}">Trước</a></li>
<li class="page-item active"><span class="page-link">${currentPage} / ${totalPages}</span></li>
<li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}"><a class="page-link" href="?page=${currentPage + 1}">Sau</a></li>
</ul></nav></c:if></main><script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script></body></html>