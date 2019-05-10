package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class ChildrenItem {

    @SerializedName("kind")
    private String kind;

    @SerializedName("data")
    private ChildrenData childrenData;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ChildrenData getChildrenData() {
        return childrenData;
    }

    public void setChildrenData(ChildrenData childrenData) {
        this.childrenData = childrenData;
    }
}
