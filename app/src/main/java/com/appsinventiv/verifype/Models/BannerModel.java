package com.appsinventiv.verifype.Models;

public class BannerModel {
    String id, imgUrl,text;

    public BannerModel(String id, String imgUrl, String text) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
