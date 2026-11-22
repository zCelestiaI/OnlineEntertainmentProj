package com.example.java4main.controller.admin;

import com.example.java4main.dao.impl.ReportDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/report")
public class ReportController extends HttpServlet {
    private final ReportDAOImpl reportDAO = new ReportDAOImpl();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Tab 1
        request.setAttribute("favoriteStats", reportDAO.favoriteReport());

        // Tab 2 (Search by Video Title)
        String videoTitle = request.getParameter("videoTitle");
        request.setAttribute("favoriteUsers", reportDAO.findFavoriteUsersByTitle(videoTitle));

        // Tab 3 (Search by Video Title for Share)
        String videoTitleShare = request.getParameter("videoTitleShare");
        request.setAttribute("shareUsers", reportDAO.findShareUsersByTitle(videoTitleShare));

        request.getRequestDispatcher("/views/admin/report.jsp").forward(request, response);
    }
}
