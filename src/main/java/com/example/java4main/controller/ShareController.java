package com.example.java4main.controller;

import com.example.java4main.entity.Share;
import com.example.java4main.entity.User;
import com.example.java4main.entity.Video;
import com.example.java4main.service.ShareService;
import com.example.java4main.service.VideoService;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet("/share")
public class ShareController extends HttpServlet {
    private final VideoService videos = new VideoService();
    private final ShareService shares = new ShareService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Video video = videos.findById(request.getParameter("videoId"));
        if (video == null || !video.isActive()) {
            response.sendError(404);
            return;
        }
        request.setAttribute("video", video);
        request.getRequestDispatcher("/views/user/share.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) SessionUtil.get(request, "currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Video video = videos.findById(request.getParameter("videoId"));
        if (video == null || !video.isActive()) {
            response.sendError(404);
            return;
        }
        String recipient = request.getParameter("emails");
        if (recipient == null || recipient.isBlank() || recipient.length() > 255) {
            request.setAttribute("message", "Hay nhap email nguoi nhan (toi da 255 ky tu).");
        } else {
            try {
                Share share = new Share();
                share.setUser(user);
                share.setVideo(video);
                share.setEmails(recipient.trim());
                share.setShareDate(new Date());
                shares.create(share);
                // Email sending has not been configured. Do not falsely claim delivery.
                request.setAttribute("message", "Da luu lich su chia se. Email tu dong chua duoc bat.");
            } catch (RuntimeException e) {
                getServletContext().log("Unable to save share event", e);
                request.setAttribute("message", "Khong the luu chia se. Hay thu lai.");
            }
        }
        request.setAttribute("video", video);
        request.getRequestDispatcher("/views/user/share.jsp").forward(request, response);
    }
}
