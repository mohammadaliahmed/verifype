package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Input {
    @SerializedName("channel_entity")
    @Expose
    private String channelEntity;
    @SerializedName("channel_message")
    @Expose
    private String channelMessage;
    @SerializedName("channel_sender")
    @Expose
    private String channelSender;
    @SerializedName("channel_type")
    @Expose
    private String channelType;
    @SerializedName("type")
    @Expose
    private String type;

    public String getChannelEntity() {
        return channelEntity;
    }

    public void setChannelEntity(String channelEntity) {
        this.channelEntity = channelEntity;
    }

    public String getChannelMessage() {
        return channelMessage;
    }

    public void setChannelMessage(String channelMessage) {
        this.channelMessage = channelMessage;
    }

    public String getChannelSender() {
        return channelSender;
    }

    public void setChannelSender(String channelSender) {
        this.channelSender = channelSender;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
