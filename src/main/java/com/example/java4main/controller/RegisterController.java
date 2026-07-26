package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import com.example.java4main.utils.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/views/user/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");

        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setAdmin(false);

        try {
            userService.create(user);
            
            // Y1.4: Gửi email chào mừng sau khi đăng ký thành công
            String subject = "Welcome to OE Entertainment!";
            String content = "<h2>Hello " + fullname + ",</h2>"
                           + "<p>Thank you for registering at Online Entertainment. Your account has been activated.</p>"
                           + "<p><b>Username:</b> " + id + "</p>"
                           + "<p>Enjoy your videos!</p>";
            
            try {
                EmailUtil.send(email, subject, content);
            } catch (Exception e) {
                System.err.println("Gửi mail chào mừng thất bại: " + e.getMessage());
            }

            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            request.setAttribute("message", "Đăng ký thất bại. ID hoặc Email có thể đã tồn tại.");
            request.getRequestDispatcher("/views/user/register.jsp")
                    .forward(request, response);
        }
    }
}
