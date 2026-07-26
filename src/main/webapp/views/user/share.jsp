<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Share Video - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <jsp:include page="../layout/menu.jsp" />

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm border-0 rounded-4">
                    <div class="card-header bg-primary text-white py-3 rounded-top-4">
                        <h5 class="mb-0"><i class="bi bi-send-fill me-2"></i> Share Video</h5>
                    </div>
                    <div class="card-body p-4">
                        <div class="mb-4 text-center">
                            <img src="${video.poster}" class="img-fluid rounded shadow-sm mb-3" style="max-height: 150px;">
                            <h6 class="fw-bold">${video.title}</h6>
                        </div>

                        <c:if test="${not empty message}">
                            <div class="alert alert-info py-2">${message}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/share" method="post">
                            <input type="hidden" name="videoId" value="${video.id}">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Recipient Emails</label>
                                <input name="emails" class="form-control" placeholder="example1@gmail.com, example2@gmail.com" required>
                                <div class="form-text">Separate multiple emails with commas or semicolons.</div>
                            </div>
                            <div class="d-grid gap-2">
                                <button class="btn btn-primary fw-bold">Send Invitation</button>
                                <a href="${pageContext.request.contextPath}/video?id=${video.id}" class="btn btn-light border">Back to Video</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
