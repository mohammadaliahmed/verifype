package com.appsinventiv.verifype.Models;

public class NotificationModel {
    String id,url,title,description,imageUrl;
    long time;

    boolean deleted;

    public NotificationModel(String id, String url, String title, String description, String imageUrl, long time) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
