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
            return em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = true ORDER BY v.createdAt DESC, v.views DESC",
                    Video.class).getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Video> findRelated(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = true AND v.id <> :id ORDER BY v.views DESC",
                    Video.class).setParameter("id", id).setMaxResults(10).getResultList();
        } finally { em.close(); }
    }

    public List<Video> findPage(int zeroBasedPage, int pageSize) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = true ORDER BY v.createdAt DESC, v.views DESC",
                    Video.class)
                    .setFirstResult(Math.max(0, zeroBasedPage) * pageSize)
                    .setMaxResults(pageSize).getResultList();
        } finally { em.close(); }
    }

    public List<Video> findByUploader(String ownerId, boolean includePrivate) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.uploader.id = :ownerId"
                    + (includePrivate ? "" : " AND v.active = true")
                    + " ORDER BY v.createdAt DESC";
            return em.createQuery(jpql, Video.class)
                    .setParameter("ownerId", ownerId).getResultList();
        } finally { em.close(); }
    }

    public void incrementViews(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Video v SET v.views = v.views + 1 WHERE v.id = :id AND v.active = true")
                    .setParameter("id", id).executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public List<Video> search(String keyword) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = true AND LOWER(v.title) LIKE LOWER(:keyword) ORDER BY v.createdAt DESC",
                    Video.class)
                    .setParameter("keyword", "%" + keyword.trim() + "%").getResultList();
        } finally { em.close(); }
    }
}
