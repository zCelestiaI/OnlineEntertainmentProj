<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${video.title} - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .video-container { background: #000; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.2); }
        .video-title { font-size: 1.4rem; font-weight: 700; margin-top: 15px; }
        body.light-mode .video-title { color: #030303; }
        body.dark-mode .video-title { color: #ffffff; }
        .action-bar { border-bottom: 1px solid #dee2e6; padding-bottom: 15px; margin-bottom: 20px; }
        body.dark-mode .action-bar { border-color: #333; }
        .description-box { border-radius: 12px; padding: 15px; font-size: 0.95rem; }
        body.light-mode .description-box { background-color: #f2f2f2; color: #333; }
        body.dark-mode .description-box { background-color: #2a2a2a; color: #eee; }
        .recommended-card { transition: background 0.2s; border-radius: 8px; cursor: pointer; padding: 5px; text-decoration: none; }
        body.light-mode .recommended-card:hover { background-color: #e5e5e5; }
        body.dark-mode .recommended-card:hover { background-color: #333; }
        .recommended-img { width: 160px; height: 90px; object-fit: cover; border-radius: 8px; }
        .recommended-title { font-size: 0.9rem; font-weight: 600; line-height: 1.2; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
        body.light-mode .recommended-title { color: #030303; }
        body.dark-mode .recommended-title { color: #ffffff; }
        .recommended-meta { font-size: 0.8rem; margin-top: 4px; }
        body.light-mode .recommended-meta { color: #606060; }
        body.dark-mode .recommended-meta { color: #aaa; }
    </style>
</head>
<body>
    <jsp:include page="../layout/menu.jsp" />

    <div class="container py-4">
        <div class="row">
            <div class="col-lg-8">
                <div class="video-container shadow">
                    <div class="ratio ratio-16x9">
                        <!-- YouTube Player Container -->
                        <div id="player"></div>
                    </div>
                </div>

                <h1 class="video-title">${video.title}</h1>
                <div class="action-bar d-flex justify-content-between align-items-center mt-2">
                    <div class="text-muted small">${video.views} views</div>
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/favorite?action=like&id=${video.id}" class="btn btn-sm btn-outline-danger rounded-pill px-3">Like</a>
                        <a href="${pageContext.request.contextPath}/share?videoId=${video.id}" class="btn btn-sm btn-outline-primary rounded-pill px-3">Share</a>
                    </div>
                </div>

                <div class="description-box shadow-sm">
                    <h6 class="fw-bold mb-2">Description</h6>
                    <p class="mb-0" style="white-space: pre-wrap;">${video.description}</p>
                </div>
            </div>

            <div class="col-lg-4 mt-4 mt-lg-0">
                <h5 class="fw-bold mb-3">Up Next</h5>
                <div class="d-flex flex-column gap-3">
                    <c:forEach var="rv" items="${recommendedVideos}" varStatus="loop">
                        <a href="${pageContext.request.contextPath}/video?id=${rv.id}"
                           class="recommended-card d-flex ${loop.first ? 'next-video-link' : ''}">
                            <img src="${rv.poster}" class="recommended-img shadow-sm" alt="${rv.title}">
                            <div class="ms-3 flex-grow-1">
                                <div class="recommended-title">${rv.title}</div>
                                <div class="recommended-meta">${rv.views} views • PolyOE</div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <!-- YouTube IFrame API -->
    <script>
        var tag = document.createElement('script');
        tag.src = "https://www.youtube.com/iframe_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

        var player;
        function onYouTubeIframeAPIReady() {
            player = new YT.Player('player', {
                height: '100%',
                width: '100%',
                videoId: '${youtubeId}',
                events: {
                    'onStateChange': onPlayerStateChange
                }
            });
        }

        function onPlayerStateChange(event) {
            // event.data == 0 nghĩa là video kết thúc (ENDED)
            if (event.data == 0) {
                const nextVideo = document.querySelector('.next-video-link');
                if (nextVideo) {
                    window.location.href = nextVideo.href;
                }
            }
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
