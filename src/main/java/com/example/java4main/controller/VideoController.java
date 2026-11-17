package com.example.java4main.controller;

import com.example.java4main.entity.Video;
import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.utils.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/video")
public class VideoController extends HttpServlet {
    private final VideoDAOImpl dao = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        Video video = dao.findById(id);
        if (video == null || !video.isActive()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        dao.incrementViews(id);
        video.setViews(video.getViews() + 1);
        request.setAttribute("video", video);
        request.setAttribute("youtubeId", video.getYoutubeId());
        request.setAttribute("recommendedVideos", dao.findRelated(id));

        String viewed = CookieUtil.get("viewedVideos", request);
        if (!java.util.Arrays.asList(viewed.split("\\|")).contains(id)) {
            String updated = viewed.isBlank() ? id : viewed + "|" + id;
            if (updated.length() < 2500) CookieUtil.add("viewedVideos", updated, 24, response);
        }
        request.getRequestDispatcher("/views/user/detail.jsp").forward(request, response);
    }
}
