package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("fetch_description")
    @Expose
    private String fetchDescription;
    @SerializedName("fetch_status")
    @Expose
    private String fetchStatus;

    public String getFetchDescription() {
        return fetchDescription;
    }

    public void setFetchDescription(String fetchDescription) {
        this.fetchDescription = fetchDescription;
    }

    public String getFetchStatus() {
        return fetchStatus;
    }

    public void setFetchStatus(String fetchStatus) {
        this.fetchStatus = fetchStatus;
    }
}
