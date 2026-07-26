package com.example.java4main.dao;

import com.example.java4main.entity.Favorite;

import java.util.List;

public interface FavoriteDAO extends GenericDAO<Favorite, Long> {

    List<Favorite> findByUser(String userId);

    List<Favorite> findByVideo(String videoId);

    Favorite findByUserAndVideo(String userId, String videoId);

}