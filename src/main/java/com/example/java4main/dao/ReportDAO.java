package com.example.java4main.dao;

import com.example.java4main.dto.FavoriteReportDTO;
import com.example.java4main.entity.Favorite;
import com.example.java4main.entity.Share;

import java.util.List;

public interface ReportDAO {

    List<FavoriteReportDTO> favoriteReport();

    List<Favorite> favoriteUsers(String videoId);

    List<Share> shareUsers(String videoId);

}