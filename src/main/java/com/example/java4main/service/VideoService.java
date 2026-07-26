package com.example.java4main.service;

import com.example.java4main.dao.VideoDAO;
import com.example.java4main.dao.impl.VideoDAOImpl;
import com.example.java4main.entity.Video;

import java.util.List;

public class VideoService {

    private final VideoDAO videoDAO = new VideoDAOImpl();

    public List<Video> findAll() {
        return videoDAO.findAll();
    }

    public Video findById(String id) {
        return videoDAO.findById(id);
    }

    public List<Video> findActive() {
        return videoDAO.findActive();
    }

    public List<Video> search(String keyword) {
        return videoDAO.search(keyword);
    }

    public Video create(Video video) {
        return videoDAO.create(video);
    }

    public Video update(Video video) {
        return videoDAO.update(video);
    }

    public void delete(String id) {
        videoDAO.delete(id);
    }
}