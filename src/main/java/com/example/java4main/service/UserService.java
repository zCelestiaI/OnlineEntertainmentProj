package com.example.java4main.service;

import com.example.java4main.dao.UserDAO;
import com.example.java4main.dao.impl.UserDAOImpl;
import com.example.java4main.entity.User;
import com.example.java4main.utils.PasswordUtil;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAOImpl();

    public List<User> findAll() { return userDAO.findAll(); }
    public User findById(String id) { return userDAO.findById(id); }
    public User findByEmail(String email) { return userDAO.findByEmail(email); }

    public User login(String id, String password) {
        if (id == null || password == null) return null;
        User user = userDAO.findById(id);
        if (user == null || !PasswordUtil.matches(password, user.getPassword())) return null;

        // Upgrade plaintext passwords from the previous academic project on login.
        if (!PasswordUtil.isHash(user.getPassword())) {
            user.setPassword(PasswordUtil.hash(password));
            user = userDAO.update(user);
        }
        return user;
    }

    public User create(User user) {
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        return userDAO.create(user);
    }

    public User update(User user) {
        User previous = userDAO.findById(user.getId());
        if (previous != null && (user.getPassword() == null || user.getPassword().isBlank())) {
            user.setPassword(previous.getPassword());
        } else if (!PasswordUtil.isHash(user.getPassword())) {
            user.setPassword(PasswordUtil.hash(user.getPassword()));
        }
        return userDAO.update(user);
    }

    public void delete(String id) { userDAO.delete(id); }
}
