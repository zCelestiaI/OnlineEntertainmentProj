package com.example.java4main.dao;

import com.example.java4main.entity.Video;
import java.util.List;

public interface VideoDAO extends GenericDAO<Video, String> {
    List<Video> findActive();
    List<Video> search(String keyword);
    List<Video> findRelated(String currentVideoId);
}
