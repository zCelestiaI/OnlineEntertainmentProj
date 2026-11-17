<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Channel - Online Entertainment</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<style>body{background:#f6f8fb}.banner{background:linear-gradient(120deg,#18365a,#346799);color:white;border-radius:16px}.thumb{height:185px;object-fit:cover}</style>
</head><body><jsp:include page="../layout/menu.jsp"/><main class="container py-4">
<div class="banner p-4 mb-4"><h1 class="h2"><c:out value="${channel.displayChannelName}"/></h1>
<div class="opacity-75">@<c:out value="${channel.id}"/></div>
<p class="mt-2 mb-0" style="white-space:pre-wrap"><c:out value="${channel.channelDescription}"/></p>
<c:if test="${not empty currentUser and currentUser.id eq channel.id}"><a class="btn btn-light btn-sm mt-3" href="${pageContext.request.contextPath}/studio">Quản lý kênh</a></c:if></div>
<h2 class="h4 mb-3">Video công khai</h2><c:if test="${empty videos}"><p class="text-muted">Kênh chưa đăng video công khai.</p></c:if>
<div class="row g-4"><c:forEach var="v" items="${videos}"><div class="col-md-6 col-lg-4">
<div class="card border-0 shadow-sm h-100">
<a href="${pageContext.request.contextPath}/video?id=${v.id}"><c:choose>
<c:when test="${not empty v.poster}"><img class="card-img-top thumb" src="<c:out value='${v.poster}'/>" alt=""></c:when>
<c:otherwise><img class="card-img-top thumb" src="${pageContext.request.contextPath}/assets/img/video-placeholder.svg" alt=""></c:otherwise>
</c:choose></a><div class="card-body"><a class="fw-semibold text-decoration-none" href="${pageContext.request.contextPath}/video?id=${v.id}"><c:out value="${v.title}"/></a>
<div class="text-muted small mt-2">${v.views} lượt xem</div></div></div></div></c:forEach></div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</body></html>