package com.example.java4main.controller.admin;

import com.example.java4main.entity.Video;
import com.example.java4main.service.VideoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/admin/video")
public class AdminVideoController extends HttpServlet {

    private final VideoService videoService = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) {
            request.setAttribute("video", videoService.findById(id));
        }

        request.setAttribute("videos", videoService.findAll());
        request.getRequestDispatcher("/views/admin/video.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if ("delete".equals(action)) {
            videoService.delete(id);
        } else {
            Video video = new Video();
            video.setId(id);
            video.setTitle(request.getParameter("title"));
            video.setDescription(request.getParameter("description"));
            video.setActive(request.getParameter("active") != null);
            
            String youtubeUrl = request.getParameter("youtubeUrl");
            String youtubeId = extractYoutubeId(youtubeUrl);
            
            if (youtubeId != null) {
                video.setPoster("https://img.youtube.com/vi/" + youtubeId + "/maxresdefault.jpg");
            } else if (request.getParameter("poster") != null && !request.getParameter("poster").isBlank()) {
                video.setPoster(request.getParameter("poster"));
            } else {
                video.setPoster("https://via.placeholder.com/480x360?text=No+Thumbnail");
            }

            if ("create".equals(action)) {
                video.setViews(0);
                videoService.create(video);
            } else if ("update".equals(action)) {
                Video existing = videoService.findById(id);
                if (existing != null) {
                    video.setViews(existing.getViews());
                    videoService.update(video);
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/video");
    }

    private String extractYoutubeId(String url) {
        if (url == null || url.isBlank()) return null;
        // Sửa lỗi Regex bằng cách escape đúng các ký tự đặc biệt
        String pattern = "(?<=watch\\?v=|v/|embed/|youtu\\.be/)[^#&?\\s]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) return matcher.group();
        return null;
    }
}
