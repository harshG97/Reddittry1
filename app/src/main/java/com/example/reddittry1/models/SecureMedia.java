package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class SecureMedia {
    @SerializedName("reddit_video")
    private RedditVideo reddit_video;

    public RedditVideo getReddit_video() {
        return reddit_video;
    }

    public void setReddit_video(RedditVideo reddit_video) {
        this.reddit_video = reddit_video;
    }


}
