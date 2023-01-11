package com.appsinventiv.verifype.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Compromise {
    @SerializedName("field_color")
    @Expose
    private String fieldColor;
    @SerializedName("field_findings")
    @Expose
    private List<String> fieldFindings = null;
    @SerializedName("field_name")
    @Expose
    private String fieldName;
    @SerializedName("field_score")
    @Expose
    private Integer fieldScore;
    @SerializedName("field_tips")
    @Expose
    private List<String> fieldTips = null;

    public String getFieldColor() {
        return fieldColor;
    }

    public void setFieldColor(String fieldColor) {
        this.fieldColor = fieldColor;
    }

    public List<String> getFieldFindings() {
        return fieldFindings;
    }

    public void setFieldFindings(List<String> fieldFindings) {
        this.fieldFindings = fieldFindings;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFieldScore() {
        return fieldScore;
    }

    public void setFieldScore(Integer fieldScore) {
        this.fieldScore = fieldScore;
    }

    public List<String> getFieldTips() {
        return fieldTips;
    }

    public void setFieldTips(List<String> fieldTips) {
        this.fieldTips = fieldTips;
    }
}
