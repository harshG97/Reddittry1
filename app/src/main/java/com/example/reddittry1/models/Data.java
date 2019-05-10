package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {
    @SerializedName("modhash")
    private String modhash;

    @SerializedName("children")
    private ArrayList<ChildrenItem> children;

    @SerializedName("before")
    private String before;

    @SerializedName("after")
    private String after;

    public String getModhash() {
        return modhash;
    }

    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    public ArrayList<ChildrenItem> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ChildrenItem> children) {
        this.children = children;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
