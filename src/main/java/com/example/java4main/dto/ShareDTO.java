package com.example.java4main.dto;

import java.io.Serializable;
import java.util.Date;

public class ShareDTO implements Serializable {

    private Long id;
    private String userId;
    private String videoId;
    private String emails;
    private Date shareDate;

    public ShareDTO() {
    }

    public ShareDTO(Long id, String userId, String videoId, String emails, Date shareDate) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.emails = emails;
        this.shareDate = shareDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }
}