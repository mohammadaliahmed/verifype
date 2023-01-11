package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FraudProfileResults {
    @SerializedName("compromise")
    @Expose
    private List<Compromise> compromise = null;
    @SerializedName("score")
    @Expose
    private Score score;

    public List<Compromise> getCompromise() {
        return compromise;
    }

    public void setCompromise(List<Compromise> compromise) {
        this.compromise = compromise;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
