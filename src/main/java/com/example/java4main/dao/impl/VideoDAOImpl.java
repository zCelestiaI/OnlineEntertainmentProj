package com.example.java4main.dao.impl;

import com.example.java4main.dao.VideoDAO;
import com.example.java4main.entity.Video;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VideoDAOImpl extends AbstractDAO<Video, String> implements VideoDAO {

    @Override
    public List<Video> findActive() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.views DESC";
            return em.createQuery(jpql, Video.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findRelated(String currentVideoId) {
        EntityManager em = XJPA.getEntityManager();
        try {
            // Loại trừ video đang xem và sắp xếp ngẫu nhiên (hoặc theo views)
            // SQL Server dùng NEWID(), nhưng JPQL chuẩn thường dùng hàm ngẫu nhiên của database
            String jpql = "SELECT v FROM Video v WHERE v.active = true AND v.id <> :cid ORDER BY v.views DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setParameter("cid", currentVideoId);
            query.setMaxResults(10);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Video> findPage(int pageNumber, int pageSize) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.active = true ORDER BY v.views DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void incrementViews(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            Video video = em.find(Video.class, id);
            if (video != null) {
                video.setViews(video.getViews() + 1);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> search(String keyword) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(:keyword) AND v.active = true";
            return em.createQuery(jpql, Video.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
