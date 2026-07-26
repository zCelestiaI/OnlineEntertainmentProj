package com.example.java4main.service;

import com.example.java4main.dao.ShareDAO;
import com.example.java4main.dao.impl.ShareDAOImpl;
import com.example.java4main.entity.Share;

import java.util.List;

public class ShareService {

    private final ShareDAO shareDAO = new ShareDAOImpl();

    public List<Share> findAll() {
        return shareDAO.findAll();
    }

    public Share findById(Long id) {
        return shareDAO.findById(id);
    }

    public List<Share> findByUser(String userId) {
        return shareDAO.findByUser(userId);
    }

    public List<Share> findByVideo(String videoId) {
        return shareDAO.findByVideo(videoId);
    }

    public Share create(Share share) {
        return shareDAO.create(share);
    }

    public Share update(Share share) {
        return shareDAO.update(share);
    }

    public void delete(Long id) {
        shareDAO.delete(id);
    }
}