package com.appsinventiv.verifype.Models;

public class SupportChatModel {
    String id, message, senderId, sendTo,name;
    long time;

    public SupportChatModel(String id, String message, String senderId, String sendTo,  String name, long time) {
        this.id = id;
        this.message = message;
        this.senderId = senderId;
        this.sendTo = sendTo;
        this.time = time;
        this.name = name;
    }

    public SupportChatModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
