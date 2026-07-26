package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Y3.7.4: Hiển thị thông tin user lấy từ session lên form
        User user = (User) SessionUtil.get(request, "currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        User currentUser = (User) SessionUtil.get(request, "currentUser");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin mới từ form
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");

        try {
            currentUser.setFullname(fullname);
            currentUser.setEmail(email);
            
            // Cập nhật Database
            userService.update(currentUser);
            
            // Cập nhật lại Session
            SessionUtil.set(request, "currentUser", currentUser);
            
            request.setAttribute("message", "Profile updated successfully!");
        } catch (Exception e) {
            request.setAttribute("message", "Failed to update profile.");
        }

        request.setAttribute("user", currentUser);
        request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
    }
}
