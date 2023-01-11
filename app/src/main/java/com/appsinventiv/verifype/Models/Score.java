package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("detailed_desciption")
    @Expose
    private String detailedDesciption;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("score_description")
    @Expose
    private String scoreDescription;

    public String getDetailedDesciption() {
        return detailedDesciption;
    }

    public void setDetailedDesciption(String detailedDesciption) {
        this.detailedDesciption = detailedDesciption;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreDescription() {
        return scoreDescription;
    }

    public void setScoreDescription(String scoreDescription) {
        this.scoreDescription = scoreDescription;
    }
}
