package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import com.example.java4main.utils.SessionUtil;
import com.example.java4main.utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) SessionUtil.get(request, "currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        if (!PasswordUtil.matches(oldPass, user.getPassword())) {
            request.setAttribute("message", "Current password is incorrect.");
        } else if (newPass == null || newPass.length() < 8) {
            request.setAttribute("message", "New password must contain at least 8 characters.");
        } else if (!newPass.equals(confirmPass)) {
            request.setAttribute("message", "New password and confirmation do not match.");
        } else {
            try {
                user.setPassword(newPass);
                userService.update(user);
                request.setAttribute("message", "Password changed successfully!");
            } catch (Exception e) {
                request.setAttribute("message", "Error updating password.");
            }
        }
        
        request.getRequestDispatcher("/views/user/change-password.jsp").forward(request, response);
    }
}
