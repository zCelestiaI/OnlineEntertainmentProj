package com.example.java4main.controller.admin;

import com.example.java4main.service.UserService;
import com.example.java4main.service.VideoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/admin", "/admin/dashboard"})
public class AdminController extends HttpServlet {

    private final UserService userService = new UserService();
    private final VideoService videoService = new VideoService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("users", userService.findAll());
        request.setAttribute("videos", videoService.findAll());

        // Fixed path: /views/admin/dashboard.jsp
        request.getRequestDispatcher("/views/admin/dashboard.jsp")
                .forward(request, response);
    }
}
