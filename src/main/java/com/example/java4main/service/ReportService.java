package com.example.java4main.service;

import com.example.java4main.dao.ReportDAO;
import com.example.java4main.dao.impl.ReportDAOImpl;
import com.example.java4main.dto.FavoriteReportDTO;
import com.example.java4main.entity.Favorite;
import com.example.java4main.entity.Share;

import java.util.List;

public class ReportService {

    private final ReportDAO dao = new ReportDAOImpl();

    public List<FavoriteReportDTO> favoriteReport() {
        return dao.favoriteReport();
    }

    public List<Favorite> favoriteUsers(String videoId) {
        return dao.favoriteUsers(videoId);
    }

    public List<Share> shareUsers(String videoId) {
        return dao.shareUsers(videoId);
    }
}
