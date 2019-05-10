package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class FeedResponse {

    @SerializedName("kind")
    private String kind;

    @SerializedName("data")
    private  Data data;

    public Data getData() {
        return data;
    }

    public String getKind() {
        return kind;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
