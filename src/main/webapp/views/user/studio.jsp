<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>Creator Studio</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<style>body{background:#f6f8fb}.card{border:0;border-radius:14px}.hero{background:#193a61;color:white;border-radius:16px}.thumb{width:110px;height:65px;object-fit:cover;border-radius:8px}</style>
</head><body><jsp:include page="../layout/menu.jsp"/><main class="container py-4">
<div class="hero p-4 mb-4 d-flex justify-content-between align-items-center"><div><div class="small text-uppercase">Creator Studio</div>
<h1 class="h3 mb-0"><c:out value="${owner.displayChannelName}"/></h1></div>
<a class="btn btn-light" href="${pageContext.request.contextPath}/channel?id=${owner.id}">Xem kênh</a></div>
<c:if test="${param.success eq '1'}"><div class="alert alert-success">Đã lưu thay đổi.</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
<div class="row g-4"><aside class="col-lg-4"><div class="card shadow-sm p-4 mb-4">
<h2 class="h5">Thông tin kênh</h2>
<form method="post" action="${pageContext.request.contextPath}/studio">
<input type="hidden" name="action" value="channel">
<label class="form-label">Tên kênh</label>
<input class="form-control mb-3" name="channelName" maxlength="100" required value="<c:out value='${owner.displayChannelName}'/>">
<label class="form-label">Giới thiệu kênh</label>
<textarea class="form-control mb-3" name="channelDescription" maxlength="500" rows="4"><c:out value="${owner.channelDescription}"/></textarea>
<button class="btn btn-outline-primary w-100">Lưu thông tin kênh</button></form></div>
<div class="card shadow-sm p-4"><h2 class="h6">Cơ chế đăng video</h2>
<p class="small text-muted mb-2">Dán link YouTube để hệ thống tự tạo ảnh thumbnail và trình phát video. Không cần tải file hay lưu trữ video trên server.</p>
<p class="small text-muted mb-0">Hỗ trợ YouTube Watch, Shorts, Live, youtu.be.</p></div></aside>
<div class="col-lg-8"><section class="card shadow-sm p-4 mb-4">
<div class="d-flex justify-content-between align-items-center"><h2 class="h5">${empty editing ? 'Đăng video mới' : 'Chỉnh sửa video'}</h2>
<c:if test="${not empty editing}"><a href="${pageContext.request.contextPath}/studio" class="btn btn-sm btn-light">Hủy sửa</a></c:if></div>
<form method="post" action="${pageContext.request.contextPath}/studio" >
<input type="hidden" name="action" value="save"><input type="hidden" name="id" value="<c:out value='${editing.id}'/>">
<label class="form-label">Tiêu đề</label><input class="form-control mb-3" name="title" maxlength="255" required value="<c:out value='${editing.title}'/>">
<label class="form-label">Mô tả</label><textarea class="form-control mb-3" name="description" maxlength="20000" rows="3"><c:out value="${editing.description}"/></textarea>
<label class="form-label">Link video YouTube</label>
<input class="form-control mb-3" type="url" name="youtubeUrl" placeholder="https://www.youtube.com/watch?v=...">
<label class="form-label">Link ảnh thumbnail tùy chọn (để trống sẽ tự lấy từ YouTube)</label><input class="form-control mb-3" type="url" name="poster" maxlength="255" placeholder="https://..." >
<label class="form-check mb-3"><input class="form-check-input" type="checkbox" name="active" ${empty editing or editing.active ? 'checked' : ''}> <span class="form-check-label">Công khai video</span></label>
<button class="btn btn-primary" type="submit">${empty editing ? 'Đăng video' : 'Lưu thay đổi'}</button>
<c:if test="${not empty editing}"><span class="small text-muted ms-2">Để trống link YouTube khi sửa để giữ video hiện tại; nhập link mới sẽ tự cập nhật thumbnail.</span></c:if>
</form></section>
<section class="card shadow-sm p-4"><h2 class="h5">Video của bạn</h2>
<c:if test="${empty videos}"><p class="text-muted">Kênh chưa có video.</p></c:if>
<c:forEach var="v" items="${videos}"><div class="d-flex gap-3 align-items-center py-3 border-bottom">
<c:choose><c:when test="${not empty v.poster}"><img class="thumb" src="<c:out value='${v.poster}'/>" alt=""></c:when>
<c:otherwise><img class="thumb" src="${pageContext.request.contextPath}/assets/img/video-placeholder.svg" alt=""></c:otherwise></c:choose>
<div class="flex-grow-1"><div class="fw-semibold"><c:out value="${v.title}"/></div>
<div class="small text-muted">${v.views} lượt xem · ${v.active ? 'Công khai' : 'Riêng tư'}</div>
<a class="small" href="${pageContext.request.contextPath}/video?id=${v.id}">Xem video</a></div>
<a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/studio?edit=${v.id}">Sửa</a>
<form method="post" action="${pageContext.request.contextPath}/studio" onsubmit="return confirm('Xóa video này?')">
<input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<c:out value='${v.id}'/>">
<button class="btn btn-sm btn-outline-danger">Xóa</button></form></div></c:forEach></section></div></div></main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</body></html>