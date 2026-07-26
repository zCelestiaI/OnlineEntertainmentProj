package com.example.java4main.controller;

import com.example.java4main.entity.Video;
import com.example.java4main.service.VideoService;
import com.example.java4main.dao.impl.VideoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"", "/", "/home", "/index"})
public class HomeController extends HttpServlet {
    private final VideoService videoService = new VideoService();
    private final VideoDAOImpl videoDAO = new VideoDAOImpl();
    private final int PAGE_SIZE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");
        int currentPage = (pageStr == null || pageStr.isBlank()) ? 1 : Integer.parseInt(pageStr);
        
        List<Video> videos;
        int totalVideos;

        if (keyword != null && !keyword.isBlank()) {
            // Tìm kiếm theo từ khóa
            videos = videoService.search(keyword);
            totalVideos = videos.size();
            // Đối với search có thể không cần phân trang hoặc xử lý riêng, ở đây tôi lấy tất cả kết quả search
        } else {
            // Hiển thị trang chủ bình thường (có phân trang)
            List<Video> allActive = videoService.findActive();
            totalVideos = allActive.size();
            videos = videoDAO.findPage(currentPage - 1, PAGE_SIZE);
        }
        
        int totalPages = (int) Math.ceil((double) totalVideos / PAGE_SIZE);
        
        request.setAttribute("videos", videos);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        
        request.getRequestDispatcher("/views/user/home.jsp").forward(request, response);
    }
}
