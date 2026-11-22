package com.example.java4main.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "Id", length = 20)
    private String id;

    @Column(name = "Password", length = 255, nullable = false)
    private String password;

    @Column(name = "Fullname", length = 50, nullable = false)
    private String fullname;

    @Column(name = "Email", length = 50, nullable = false)
    private String email;

    @Column(name = "Admin", nullable = false)
    private boolean admin;

    // Fix: Thêm cascade REMOVE
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    // Fix: Thêm cascade REMOVE
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Share> shares;
}
