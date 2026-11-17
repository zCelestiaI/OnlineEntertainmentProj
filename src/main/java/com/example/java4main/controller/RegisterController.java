package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final UserService users = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = value(request, "id");
        String password = request.getParameter("password");
        String fullname = value(request, "fullname");
        String email = value(request, "email").toLowerCase();
        if (!id.matches("[A-Za-z0-9_]{3,20}") || fullname.isBlank() || fullname.length() > 50
                || email.length() > 50 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || password == null || password.length() < 8) {
            request.setAttribute("message", "Vui long nhap ID 3-20 ky tu, email hop le va mat khau it nhat 8 ky tu.");
            doGet(request, response);
            return;
        }
        try {
            if (users.findById(id) != null || users.findByEmail(email) != null) {
                throw new IllegalArgumentException("ID hoac email da duoc su dung.");
            }
            User user = new User();
            user.setId(id);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setChannelName(fullname);
            user.setEmail(email);
            user.setAdmin(false);
            users.create(user);
            response.sendRedirect(request.getContextPath() + "/login?registered=1");
        } catch (Exception e) {
            request.setAttribute("message", "Dang ky that bai. Kiem tra ID, email va mat khau.");
            doGet(request, response);
        }
    }

    private String value(HttpServletRequest req, String key) {
        String s = req.getParameter(key);
        return s == null ? "" : s.trim();
    }
}
