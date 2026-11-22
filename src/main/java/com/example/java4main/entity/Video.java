package com.example.java4main.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Video")
@ToString(exclude = {"favorites", "shares", "uploader"})
@EqualsAndHashCode(exclude = {"favorites", "shares", "uploader"})
public class Video {
    private static final Pattern YOUTUBE_ID = Pattern.compile(
            "(?:[?&]v=|youtu\\.be/|/embed/|/shorts/|/vi/)([A-Za-z0-9_-]{11})");

    @Id
    @Nationalized
    @Column(name = "Id", length = 20)
    private String id;

    @Nationalized
    @Column(name = "Title", length = 255, nullable = false)
    private String title;

    @Nationalized
    @Column(name = "Poster", length = 255)
    private String poster;

    @Column(name = "Views", nullable = false)
    @Builder.Default
    private Integer views = 0;

    @Nationalized
    @JdbcTypeCode(SqlTypes.LONG32NVARCHAR)
    @Column(name = "Description")
    private String description;

    @Column(name = "Active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UploaderId", referencedColumnName = "Id")
    private User uploader;

    @Nationalized
    @Column(name = "SourceUrl", length = 1024)
    private String sourceUrl;

    @Nationalized
    @Column(name = "SourceType", length = 20)
    private String sourceType;

    @Column(name = "CreatedAt")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Share> shares;

    @Transient
    public String getYoutubeId() {
        String source = sourceUrl == null ? "" : sourceUrl;
        Matcher m = YOUTUBE_ID.matcher(source);
        if (m.find()) return m.group(1);
        m = YOUTUBE_ID.matcher(poster == null ? "" : poster);
        return m.find() ? m.group(1) : null;
    }

    @Transient
    public String getUploaderDisplayName() {
        return uploader == null ? "Online Entertainment" : uploader.getDisplayChannelName();
    }
}
