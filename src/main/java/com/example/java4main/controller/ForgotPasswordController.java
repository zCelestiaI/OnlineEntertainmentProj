package com.example.java4main.controller;

import com.example.java4main.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/views/user/forgot-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        String email = request.getParameter("email");

        // Logic xử lý quên mật khẩu (Gửi mail hoặc hiển thị mật khẩu)
        // Hiện tại tạm thời redirect về login
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
