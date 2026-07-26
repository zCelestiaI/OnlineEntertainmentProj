package com.example.java4main.dao.impl;

import com.example.java4main.dao.ShareDAO;
import com.example.java4main.entity.Share;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ShareDAOImpl extends AbstractDAO<Share, Long> implements ShareDAO {

    @Override
    public List<Share> findByUser(String userId) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT s FROM Share s WHERE s.user.id = :userId";

            return em.createQuery(jpql, Share.class)
                    .setParameter("userId", userId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Share> findByVideo(String videoId) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT s FROM Share s WHERE s.video.id = :videoId";

            return em.createQuery(jpql, Share.class)
                    .setParameter("videoId", videoId)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}