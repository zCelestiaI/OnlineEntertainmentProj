package com.example.java4main.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
@ToString(exclude = {"favorites", "shares"})
@EqualsAndHashCode(exclude = {"favorites", "shares"})
public class User {
    @Id
    @Nationalized
    @Column(name = "Id", length = 20)
    private String id;

    @Nationalized
    @Column(name = "Password", length = 255, nullable = false)
    private String password;

    @Nationalized
    @Column(name = "Fullname", length = 50, nullable = false)
    private String fullname;

    @Nationalized
    @Column(name = "Email", length = 50, nullable = false)
    private String email;

    @Column(name = "Admin", nullable = false)
    private boolean admin;

    @Nationalized
    @Column(name = "ChannelName", length = 100)
    private String channelName;

    @Nationalized
    @Column(name = "ChannelDescription", length = 500)
    private String channelDescription;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Share> shares;

    @Transient
    public String getDisplayChannelName() {
        return channelName == null || channelName.isBlank() ? fullname : channelName;
    }
}
