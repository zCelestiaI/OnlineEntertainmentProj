package com.example.java4main.controller;

import com.example.java4main.entity.User;
import com.example.java4main.service.FavoriteService;
import com.example.java4main.utils.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/favorite")
public class FavoriteController extends HttpServlet {
    private final FavoriteService favoriteService = new FavoriteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User user = (User) SessionUtil.get(request, "currentUser");
        String action = request.getParameter("action");
        String videoId = request.getParameter("id");

        // Nếu nhấn Like/Unlike mà chưa login -> Đưa đi Login
        if ("like".equals(action) || "unlike".equals(action)) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            
            if ("like".equals(action)) {
                favoriteService.like(user.getId(), videoId);
            } else {
                favoriteService.unlike(user.getId(), videoId);
            }
            response.sendRedirect(request.getContextPath() + "/video?id=" + videoId);
            return;
        }

        // Nếu là truy cập trang My Favorites (phải login)
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.setAttribute("favorites", favoriteService.findByUser(user.getId()));
        request.getRequestDispatcher("/views/user/favorite.jsp").forward(request, response);
    }
}
