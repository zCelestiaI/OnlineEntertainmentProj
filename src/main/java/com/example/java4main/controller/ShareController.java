package com.example.java4main.controller;

import com.example.java4main.entity.Share;
import com.example.java4main.entity.User;
import com.example.java4main.entity.Video;
import com.example.java4main.service.ShareService;
import com.example.java4main.service.VideoService;
import com.example.java4main.utils.EmailUtil;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet("/share")
public class ShareController extends HttpServlet {
    private final VideoService videoService = new VideoService();
    private final ShareService shareService = new ShareService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String videoId = request.getParameter("videoId");
        Video video = videoService.findById(videoId);
        if (video == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.setAttribute("video", video);
        request.getRequestDispatcher("/views/user/share.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String videoId = request.getParameter("videoId");
        String emails = request.getParameter("emails");
        User user = (User) SessionUtil.get(request, "currentUser");
        Video video = videoService.findById(videoId);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // 1. Gửi mail
            String subject = "OE Entertainment - Shared Video: " + video.getTitle();
            String content = "<h2>Hello!</h2>"
                           + "<p>Your friend <b>" + user.getFullname() + "</b> shared a video with you:</p>"
                           + "<p><a href='http://localhost:8080" + request.getContextPath() + "/video?id=" + videoId + "'>CLICK HERE TO WATCH</a></p>";
            
            EmailUtil.send(emails, subject, content);

            // 2. Lưu vào Database (Để hiện lên tab Shared Friends trong báo cáo)
            Share share = new Share();
            share.setUser(user);
            share.setVideo(video);
            share.setEmails(emails);
            share.setShareDate(new Date());
            shareService.create(share);

            request.setAttribute("message", "Video shared successfully and recorded!");
        } catch (Exception e) {
            request.setAttribute("message", "Email sent, but failed to record to database: " + e.getMessage());
        }
        
        request.setAttribute("video", video);
        request.getRequestDispatcher("/views/user/share.jsp").forward(request, response);
    }
}
