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
@Table(name = "Video")
public class Video {
    @Id
    @Column(name = "Id", length = 20)
    private String id;

    @Column(name = "Title", length = 255, nullable = false)
    private String title;

    @Column(name = "Poster", length = 255)
    private String poster;

    @Column(name = "Views", nullable = false)
    private Integer views = 0;

    @Column(name = "Description", columnDefinition = "nvarchar(max)")
    private String description;

    @Column(name = "Active", nullable = false)
    private boolean active = true;

    // Fix: Thêm cascade REMOVE để tự động xóa các bản ghi Favorite liên quan
    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    // Fix: Thêm cascade REMOVE để tự động xóa các bản ghi Share liên quan
    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Share> shares;
}
