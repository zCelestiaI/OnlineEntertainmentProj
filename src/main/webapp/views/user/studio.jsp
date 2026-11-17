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
<div class="card shadow-sm p-4"><h2 class="h6">Tải video trực tiếp</h2>
<c:choose><c:when test="${cloudinaryConfigured}"><span class="badge text-bg-success">Cloudinary đã kết nối</span></c:when>
<c:otherwise><p class="small text-muted">Chưa cấu hình Cloudinary. Hiện bạn có thể đăng bằng link YouTube; xem DEPLOYMENT.md để bật tải file.</p></c:otherwise></c:choose>
<p class="small text-muted mb-0">File MP4 / WebM / MOV, tối đa 15 MB.</p></div></aside>
<div class="col-lg-8"><section class="card shadow-sm p-4 mb-4">
<div class="d-flex justify-content-between align-items-center"><h2 class="h5">${empty editing ? 'Đăng video mới' : 'Chỉnh sửa video'}</h2>
<c:if test="${not empty editing}"><a href="${pageContext.request.contextPath}/studio" class="btn btn-sm btn-light">Hủy sửa</a></c:if></div>
<form method="post" action="${pageContext.request.contextPath}/studio" enctype="multipart/form-data">
<input type="hidden" name="action" value="save"><input type="hidden" name="id" value="<c:out value='${editing.id}'/>">
<label class="form-label">Tiêu đề</label><input class="form-control mb-3" name="title" maxlength="255" required value="<c:out value='${editing.title}'/>">
<label class="form-label">Mô tả</label><textarea class="form-control mb-3" name="description" maxlength="20000" rows="3"><c:out value="${editing.description}"/></textarea>
<label class="form-label">Link video YouTube (không bắt buộc nếu tải file)</label>
<input class="form-control mb-3" type="url" name="youtubeUrl" placeholder="https://www.youtube.com/watch?v=...">
<label class="form-label">Hoặc chọn file video</label>
<input class="form-control mb-3" type="file" name="videoFile" accept="video/mp4,video/webm,video/quicktime" ${cloudinaryConfigured ? '' : 'disabled'}>
<label class="form-label">Ảnh bìa (URL HTTPS, không bắt buộc)</label><input class="form-control mb-3" type="url" name="poster" maxlength="255" placeholder="https://..." value="<c:out value='${editing.poster}'/>">
<label class="form-check mb-3"><input class="form-check-input" type="checkbox" name="active" ${empty editing or editing.active ? 'checked' : ''}> <span class="form-check-label">Công khai video</span></label>
<button class="btn btn-primary" type="submit">${empty editing ? 'Đăng video' : 'Lưu thay đổi'}</button>
<c:if test="${not empty editing}"><span class="small text-muted ms-2">Không nhập nguồn mới để giữ video hiện tại.</span></c:if>
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