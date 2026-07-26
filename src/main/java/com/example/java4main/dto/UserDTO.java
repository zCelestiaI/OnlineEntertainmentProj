package com.example.java4main.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private String id;
    private String fullname;
    private String email;
    private Boolean admin;

    public UserDTO() {
    }

    public UserDTO(String id, String fullname, String email, Boolean admin) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}