package com.example.java4main.dao.impl;

import com.example.java4main.dao.ReportDAO;
import com.example.java4main.dto.FavoriteReportDTO;
import com.example.java4main.entity.Favorite;
import com.example.java4main.entity.Share;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<FavoriteReportDTO> favoriteReport() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT new com.example.java4main.dto.FavoriteReportDTO(f.video.title, count(f), max(f.likeDate), min(f.likeDate)) " +
                          "FROM Favorite f GROUP BY f.video.title";
            return em.createQuery(jpql, FavoriteReportDTO.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Favorite> findFavoriteUsersByTitle(String title) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT f FROM Favorite f";
            if (title != null && !title.isBlank()) {
                jpql += " WHERE f.video.title LIKE :title";
            }
            TypedQuery<Favorite> query = em.createQuery(jpql, Favorite.class);
            if (title != null && !title.isBlank()) {
                query.setParameter("title", "%" + title + "%");
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Share> findShareUsersByTitle(String title) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT s FROM Share s";
            if (title != null && !title.isBlank()) {
                jpql += " WHERE s.video.title LIKE :title";
            }
            TypedQuery<Share> query = em.createQuery(jpql, Share.class);
            if (title != null && !title.isBlank()) {
                query.setParameter("title", "%" + title + "%");
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Giữ các phương thức cũ để tránh lỗi compile nếu có chỗ khác gọi
    @Override public List<Favorite> favoriteUsers(String videoId) { return findFavoriteUsersByTitle(null); }
    @Override public List<Share> shareUsers(String videoId) { return findShareUsersByTitle(null); }
}
