package com.example.java4main.controller;

import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.entity.Video;
import com.example.java4main.service.VideoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"", "/", "/home", "/index"})
public class HomeController extends HttpServlet {
    private static final int PAGE_SIZE = 9;
    private final VideoService service = new VideoService();
    private final VideoDAOImpl dao = new VideoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String rawPage = request.getParameter("page");
        int currentPage;
        try { currentPage = Math.max(1, Integer.parseInt(rawPage)); }
        catch (Exception e) { currentPage = 1; }
        List<Video> all = keyword != null && !keyword.isBlank()
                ? service.search(keyword.trim()) : service.findActive();
        int totalPages = Math.max(1, (all.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        currentPage = Math.min(currentPage, totalPages);
        int start = (currentPage - 1) * PAGE_SIZE;
        List<Video> visible = all.subList(start, Math.min(all.size(), start + PAGE_SIZE));
        request.setAttribute("videos", visible);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/views/user/home.jsp").forward(request, response);
    }
}
