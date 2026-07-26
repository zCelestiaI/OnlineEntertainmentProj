package com.example.java4main.controller;

import com.example.java4main.entity.Video;
import com.example.java4main.service.VideoService;
import com.example.java4main.utils.CookieUtil;
import com.example.java4main.dao.impl.VideoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/video")
public class VideoController extends HttpServlet {
    private final VideoDAOImpl videoDAO = new VideoDAOImpl();
    private final VideoService videoService = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // 1. Tăng view & Lấy video hiện tại
        videoDAO.incrementViews(id);
        Video video = videoService.findById(id);
        if (video == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.setAttribute("video", video);

        // 2. Trích xuất ID YouTube
        if (video.getPoster() != null && video.getPoster().contains("img.youtube.com/vi/")) {
            String[] parts = video.getPoster().split("/");
            String youtubeId = parts[parts.length - 2];
            request.setAttribute("youtubeId", youtubeId);
        }

        // 3. Lấy danh sách video đề xuất (LOẠI TRỪ VIDEO ĐANG XEM)
        // Fix: Sử dụng findRelated để tránh hiện tượng lặp vòng 2 video
        List<Video> recommendedVideos = videoDAO.findRelated(id);
        request.setAttribute("recommendedVideos", recommendedVideos);

        // 4. Xử lý Cookie
        String viewedIds = CookieUtil.get("viewedVideos", request);
        if (!viewedIds.contains(id)) {
            viewedIds = viewedIds.isEmpty() ? id : viewedIds + "|" + id;
            CookieUtil.add("viewedVideos", viewedIds, 24, response);
        }

        request.getRequestDispatcher("/views/user/detail.jsp").forward(request, response);
    }
}
