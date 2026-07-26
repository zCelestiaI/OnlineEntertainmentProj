package com.example.java4main.controller.admin;

import com.example.java4main.entity.User;
import com.example.java4main.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/user")
public class AdminUserController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        if (id != null && !id.isBlank()) {
            request.setAttribute("user", userService.findById(id));
        }

        request.setAttribute("users", userService.findAll());
        request.getRequestDispatcher("/views/admin/user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if ("delete".equals(action)) {
            userService.delete(id);
        } else {
            User user = new User();
            user.setId(id);
            user.setPassword(request.getParameter("password"));
            user.setFullname(request.getParameter("fullname"));
            user.setEmail(request.getParameter("email"));
            
            // Loại bỏ option tạo Admin mới: mặc định là false
            // Hoặc lấy giá trị cũ nếu đang update một admin
            User existing = userService.findById(id);
            if (existing != null) {
                user.setAdmin(existing.isAdmin());
            } else {
                user.setAdmin(false); 
            }

            if ("create".equals(action)) {
                userService.create(user);
            } else if ("update".equals(action)) {
                userService.update(user);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
}
