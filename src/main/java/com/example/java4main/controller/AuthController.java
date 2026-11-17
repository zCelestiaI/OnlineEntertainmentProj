package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import com.example.java4main.utils.CookieUtil;
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
            request.getRequestDispatcher("/views/user/login.jsp")
                    .forward(request, response);
            return;
        }

        if (uri.equals("/logout")) {
            // Xóa cookie "rememberMe" khi logout
            CookieUtil.remove("rememberMe", response);
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
        boolean rememberMe = request.getParameter("rememberMe") != null;

        User user = userService.login(id, password);

        if (user != null) { // UserService has already verified the supplied password
            request.getSession();
            request.changeSessionId(); // Prevent session fixation after sign-in
            SessionUtil.set(request, "currentUser", user);
            
            // Xử lý Remember Me
            if (rememberMe) {
                CookieUtil.add("rememberMe", user.getId(), 24 * 7, response); // Lưu trong 7 ngày
            } else {
                CookieUtil.remove("rememberMe", response);
            }
            
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("message", "Sai tài khoản hoặc mật khẩu.");
            request.getRequestDispatcher("/views/user/login.jsp")
                    .forward(request, response);
        }
    }
}
