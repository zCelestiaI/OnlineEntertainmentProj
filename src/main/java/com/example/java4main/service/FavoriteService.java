package com.example.java4main.service;

import com.example.java4main.dao.FavoriteDAO;
import com.example.java4main.dao.impl.FavoriteDAOImpl;
import com.example.java4main.entity.Favorite;
import com.example.java4main.entity.User;
import com.example.java4main.entity.Video;

import java.util.Date;
import java.util.List;

public class FavoriteService {

    private final FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    private final UserService userService = new UserService();
    private final VideoService videoService = new VideoService();

    public List<Favorite> findAll() {
        return favoriteDAO.findAll();
    }

    public Favorite findById(Long id) {
        return favoriteDAO.findById(id);
    }

    public List<Favorite> findByUser(String userId) {
        return favoriteDAO.findByUser(userId);
    }

    public Favorite findByUserAndVideo(String userId, String videoId) {
        return favoriteDAO.findByUserAndVideo(userId, videoId);
    }

    // Logic xử lý nút LIKE
    public void like(String userId, String videoId) {
        Favorite existing = favoriteDAO.findByUserAndVideo(userId, videoId);
        if (existing == null) {
            User user = userService.findById(userId);
            Video video = videoService.findById(videoId);
            if (user != null && video != null) {
                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setVideo(video);
                favorite.setLikeDate(new Date());
                favoriteDAO.create(favorite);
            }
        }
    }

    // Logic xử lý nút UNLIKE
    public void unlike(String userId, String videoId) {
        Favorite existing = favoriteDAO.findByUserAndVideo(userId, videoId);
        if (existing != null) {
            favoriteDAO.delete(existing.getId());
        }
    }

    public Favorite create(Favorite favorite) {
        return favoriteDAO.create(favorite);
    }

    public void delete(Long id) {
        favoriteDAO.delete(id);
    }
}
