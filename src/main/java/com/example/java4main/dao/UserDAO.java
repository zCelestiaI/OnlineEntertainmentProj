package com.example.java4main.dao;

import com.example.java4main.entity.User;

public interface UserDAO extends GenericDAO<User, String> {

    User findByEmail(String email);

}