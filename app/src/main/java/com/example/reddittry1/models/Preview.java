package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class Preview {
    @SerializedName("reddit_video_preview")
    private RedditVideoPreview redditVideoPreview;

    public RedditVideoPreview getRedditVideoPreview() {
        return redditVideoPreview;
    }

    public void setRedditVideoPreview(RedditVideoPreview redditVideoPreview) {
        this.redditVideoPreview = redditVideoPreview;
    }
}
