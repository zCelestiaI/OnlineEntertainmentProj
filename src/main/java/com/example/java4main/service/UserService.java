package com.example.java4main.service;

import com.example.java4main.dao.UserDAO;
import com.example.java4main.dao.impl.UserDAOImpl;
import com.example.java4main.entity.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(String id) {
        return userDAO.findById(id);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User login(String id, String password) {
        User user = userDAO.findById(id);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    public User create(User user) {
        return userDAO.create(user);
    }

    public User update(User user) {
        return userDAO.update(user);
    }

    public void delete(String id) {
        userDAO.delete(id);
    }
}