package com.example.java4main.dao.impl;

import com.example.java4main.dao.GenericDAO;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractDAO<T, ID> implements GenericDAO<T, ID> {

    protected Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public T create(T entity) {
        EntityManager em = XJPA.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = XJPA.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            entity = em.merge(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(ID id) {
        EntityManager em = XJPA.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            T entity = em.find(entityClass, id);

            if (entity != null) {
                em.remove(entity);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(ID id) {
        EntityManager em = XJPA.getEntityManager();

        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } finally {
            em.close();
        }
    }
}