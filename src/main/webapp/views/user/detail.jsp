<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Online Entertainment - Video</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<style>body{background:#f6f8fb}.player{background:#000;border-radius:14px;overflow:hidden}.side-thumb{width:148px;height:83px;object-fit:cover;border-radius:8px}</style>
</head><body><jsp:include page="../layout/menu.jsp"/><main class="container py-4"><div class="row g-4"><div class="col-lg-8">
<div class="ratio ratio-16x9 player"><c:choose>
<c:when test="${not empty youtubeId}"><iframe src="https://www.youtube-nocookie.com/embed/${youtubeId}" title="Video" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></c:when>
<c:otherwise><div class="text-white d-flex justify-content-center align-items-center">Video hiện chưa có nguồn phát.</div></c:otherwise>
</c:choose></div><h1 class="h3 fw-bold mt-3"><c:out value="${video.title}"/></h1>
<div class="d-flex justify-content-between flex-wrap gap-2 mb-3">
<div><c:choose><c:when test="${not empty video.uploader}"><a class="fw-semibold" href="${pageContext.request.contextPath}/channel?id=${video.uploader.id}"><c:out value="${video.uploaderDisplayName}"/></a></c:when>
<c:otherwise><span>Online Entertainment</span></c:otherwise></c:choose><span class="text-muted small ms-2">${video.views} lượt xem</span></div>
<div class="d-flex gap-2"><a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/favorite?action=like&id=${video.id}">Thích</a>
<a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/share?videoId=${video.id}">Chia sẻ</a>
<c:if test="${not empty currentUser and not empty video.uploader and currentUser.id eq video.uploader.id}"><a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/studio?edit=${video.id}">Sửa</a></c:if></div></div>
<div class="bg-white rounded p-3"><h2 class="h6">Mô tả</h2><p style="white-space:pre-wrap"><c:out value="${video.description}"/></p></div></div>
<aside class="col-lg-4"><h2 class="h5">Video đề xuất</h2><c:forEach var="rv" items="${recommendedVideos}">
<a href="${pageContext.request.contextPath}/video?id=${rv.id}" class="d-flex gap-2 my-3 text-body text-decoration-none">
<c:choose><c:when test="${not empty rv.poster}"><img class="side-thumb" src="<c:out value='${rv.poster}'/>" alt=""></c:when>
<c:otherwise><img class="side-thumb" src="${pageContext.request.contextPath}/assets/img/video-placeholder.svg" alt=""></c:otherwise></c:choose>
<div class="small"><b><c:out value="${rv.title}"/></b><div class="text-muted"><c:out value="${rv.uploaderDisplayName}"/></div><div class="text-muted">${rv.views} lượt xem</div></div></a></c:forEach></aside></div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script></body></html>