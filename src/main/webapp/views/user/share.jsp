<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html><html lang="vi"><head><meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Chia sẻ video - Online Entertainment</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><jsp:include page="../layout/menu.jsp"/>
<main class="container py-5"><div class="row justify-content-center"><div class="col-md-7">
<section class="card shadow-sm border-0 rounded-4 p-4">
<h1 class="h4 fw-bold mb-3">Chia sẻ video</h1>
<h2 class="h6 mb-3"><c:out value="${video.title}"/></h2>
<div class="mb-3">
<label class="form-label">Đường dẫn chia sẻ</label>
<div class="input-group">
<input id="shareLink" class="form-control" readonly>
<button class="btn btn-outline-primary" type="button" onclick="navigator.clipboard.writeText(document.getElementById('shareLink').value)">Sao chép</button>
</div>
<div class="form-text">Gửi đường dẫn này trực tiếp cho bạn bè. Không cần gửi email từ website.</div></div>
<c:if test="${not empty message}"><div class="alert alert-info"><c:out value="${message}"/></div></c:if>
<details class="mt-2"><summary>Ghi lại lượt chia sẻ qua email (chưa tự động gửi)</summary>
<form action="${pageContext.request.contextPath}/share" method="post" class="mt-3">
<input type="hidden" name="videoId" value="${video.id}">
<label class="form-label">Địa chỉ email người nhận</label>
<input type="text" name="emails" maxlength="255" class="form-control mb-2" placeholder="example@gmail.com" required>
<button class="btn btn-outline-secondary">Lưu lịch sử chia sẻ</button>
</form>
</details>
<a href="${pageContext.request.contextPath}/video?id=${video.id}" class="btn btn-light mt-3">Quay lại video</a>
</section></div></div></main>
<script>document.getElementById('shareLink').value = window.location.origin + '${pageContext.request.contextPath}/video?id=${video.id}';</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script></body></html>