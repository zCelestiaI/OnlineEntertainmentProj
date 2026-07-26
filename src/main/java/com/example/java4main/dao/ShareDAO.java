package com.example.java4main.dao;

import com.example.java4main.entity.Share;

import java.util.List;

public interface ShareDAO extends GenericDAO<Share, Long> {

    List<Share> findByUser(String userId);

    List<Share> findByVideo(String videoId);

}