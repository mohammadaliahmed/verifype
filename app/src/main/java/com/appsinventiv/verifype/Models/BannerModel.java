package com.appsinventiv.verifype.Models;

public class BannerModel {
    String id, imageUrl,message,url;

    public BannerModel(String id, String imageUrl, String message, String url) {
        this.id = id;
        this.url = url;
        this.imageUrl = imageUrl;
        this.message = message;
    }

    public BannerModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
