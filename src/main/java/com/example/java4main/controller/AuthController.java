package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/login",
        "/logout"
})
public class AuthController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getServletPath();

        if (uri.equals("/login")) {
            // Fix: Changed back to /views/user/login.jsp
            request.getRequestDispatcher("/views/user/login.jsp")
                    .forward(request, response);
            return;
        }

        if (uri.equals("/logout")) {
            SessionUtil.invalidate(request);
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String password = request.getParameter("password");

        User user = userService.login(id, password);

        if (user == null) {
            request.setAttribute("message", "Sai tài khoản hoặc mật khẩu.");
            request.getRequestDispatcher("/views/user/login.jsp")
                    .forward(request, response);
            return;
        }

        SessionUtil.set(request, "currentUser", user);
        
        if (user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
