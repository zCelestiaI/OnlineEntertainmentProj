package com.example.java4main.dto;

import java.io.Serializable;
import java.util.Date;

public class FavoriteDTO implements Serializable {

    private Long id;
    private String userId;
    private String videoId;
    private Date likeDate;

    public FavoriteDTO() {
    }

    public FavoriteDTO(Long id, String userId, String videoId, Date likeDate) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.likeDate = likeDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Date getLikeDate() {
        return likeDate;
    }

    public void setLikeDate(Date likeDate) {
        this.likeDate = likeDate;
    }
}