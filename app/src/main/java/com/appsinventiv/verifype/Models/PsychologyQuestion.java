package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PsychologyQuestion {
    @SerializedName("options")
    @Expose
    private List<String> options = null;
    @SerializedName("options_type")
    @Expose
    private String optionsType;
    @SerializedName("question")
    @Expose
    private String question;


    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getOptionsType() {
        return optionsType;
    }

    public void setOptionsType(String optionsType) {
        this.optionsType = optionsType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
