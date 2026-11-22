package com.example.java4main.controller;

import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/channel")
public class ChannelController extends HttpServlet {
    private final UserService users = new UserService();
    private final VideoDAOImpl videos = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User owner = id == null ? null : users.findById(id);
        if (owner == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("channel", owner);
        request.setAttribute("videos", videos.findByUploader(id, false));
        request.getRequestDispatcher("/views/user/channel.jsp").forward(request, response);
    }
}
