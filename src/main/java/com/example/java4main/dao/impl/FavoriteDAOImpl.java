package com.example.java4main.dao.impl;

import com.example.java4main.dao.FavoriteDAO;
import com.example.java4main.entity.Favorite;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class FavoriteDAOImpl extends AbstractDAO<Favorite, Long> implements FavoriteDAO {

    @Override
    public List<Favorite> findByUser(String userId) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT f FROM Favorite f WHERE f.user.id = :userId";

            return em.createQuery(jpql, Favorite.class)
                    .setParameter("userId", userId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Favorite> findByVideo(String videoId) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT f FROM Favorite f WHERE f.video.id = :videoId";

            return em.createQuery(jpql, Favorite.class)
                    .setParameter("videoId", videoId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public Favorite findByUserAndVideo(String userId, String videoId) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT f FROM Favorite f "
                    + "WHERE f.user.id = :userId "
                    + "AND f.video.id = :videoId";

            return em.createQuery(jpql, Favorite.class)
                    .setParameter("userId", userId)
                    .setParameter("videoId", videoId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}