package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("tips")
    @Expose
    private List<String> tips = null;
    @SerializedName("fraud_profile")
    @Expose
    private FraudProfile fraudProfile;
    @SerializedName("psychology_questions")
    @Expose
    private List<PsychologyQuestion> psychologyQuestions = null;

    public List<PsychologyQuestion> getPsychologyQuestions() {
        return psychologyQuestions;
    }

    public void setPsychologyQuestions(List<PsychologyQuestion> psychologyQuestions) {
        this.psychologyQuestions = psychologyQuestions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }


    public FraudProfile getFraudProfile() {
        return fraudProfile;
    }

    public void setFraudProfile(FraudProfile fraudProfile) {
        this.fraudProfile = fraudProfile;
    }
}
