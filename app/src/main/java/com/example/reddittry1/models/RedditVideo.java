package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class RedditVideo {
    @SerializedName("fallback_url")
    private String secureMediaVideoUrl;

    public String getSecureMediaVideoUrl() {
        return secureMediaVideoUrl;
    }

    public void setSecureMediaVideoUrl(String secureMediaVideoUrl) {
        this.secureMediaVideoUrl = secureMediaVideoUrl;
    }
}
