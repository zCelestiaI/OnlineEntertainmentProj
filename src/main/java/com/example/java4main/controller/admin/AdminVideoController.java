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
    private static final Pattern YOUTUBE = Pattern.compile(
            "(?:[?&]v=|youtu\\.be/|/embed/|/shorts/)([A-Za-z0-9_-]{11})");
    private final VideoService videos = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) request.setAttribute("video", videos.findById(id));
        request.setAttribute("videos", videos.findAll());
        request.getRequestDispatcher("/views/admin/video.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        if (id == null || id.isBlank() || id.length() > 20) {
            response.sendError(400, "Video ID is required (up to 20 characters)");
            return;
        }
        if ("delete".equals(action)) {
            videos.delete(id);
            response.sendRedirect(request.getContextPath() + "/admin/video");
            return;
        }
        Video existing = videos.findById(id);
        boolean creating = "create".equals(action);
        if (creating && existing != null) {
            response.sendError(409, "Video ID already exists");
            return;
        }
        if (!creating && existing == null) {
            response.sendError(404, "Video does not exist");
            return;
        }
        Video video = creating ? new Video() : existing;
        if (creating) {
            video.setId(id);
            video.setViews(0);
        }
        String title = request.getParameter("title");
        if (title == null || title.isBlank() || title.length() > 255) {
            response.sendError(400, "Title is required");
            return;
        }
        video.setTitle(title.trim());
        video.setDescription(request.getParameter("description"));
        video.setActive(request.getParameter("active") != null);
        String youtubeUrl = request.getParameter("youtubeUrl");
        if (youtubeUrl != null && !youtubeUrl.isBlank()) {
            Matcher matcher = YOUTUBE.matcher(youtubeUrl);
            if (!matcher.find()) {
                response.sendError(400, "Please use a valid YouTube URL");
                return;
            }
            String youtubeId = matcher.group(1);
            video.setSourceType("YOUTUBE");
            video.setSourceUrl("https://www.youtube.com/watch?v=" + youtubeId);
            video.setPoster("https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg");
        }
        String poster = request.getParameter("poster");
        if (poster != null && poster.startsWith("https://") && poster.length() <= 255) {
            video.setPoster(poster);
        }
        // Existing video uploader, source, and createdAt are preserved on admin updates.
        if (creating) videos.create(video);
        else videos.update(video);
        response.sendRedirect(request.getContextPath() + "/admin/video");
    }
}
