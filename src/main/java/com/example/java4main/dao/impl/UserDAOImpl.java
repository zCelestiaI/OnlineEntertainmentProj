package com.example.java4main.dao.impl;

import com.example.java4main.dao.UserDAO;
import com.example.java4main.entity.User;
import com.example.java4main.utils.XJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDAOImpl extends AbstractDAO<User, String> implements UserDAO {

    @Override
    public User findByEmail(String email) {
        EntityManager em = XJPA.getEntityManager();

        try {
            String jpql = "SELECT u FROM User u WHERE u.email = :email";

            return em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}