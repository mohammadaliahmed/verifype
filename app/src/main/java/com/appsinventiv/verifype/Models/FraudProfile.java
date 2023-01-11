package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FraudProfile {
    @SerializedName("fraud_profile_results")
    @Expose
    private FraudProfileResults fraudProfileResults;
    @SerializedName("status")
    @Expose
    private Status status;

    public FraudProfileResults getFraudProfileResults() {
        return fraudProfileResults;
    }

    public void setFraudProfileResults(FraudProfileResults fraudProfileResults) {
        this.fraudProfileResults = fraudProfileResults;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
