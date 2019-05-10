package com.example.reddittry1.models;

import com.google.gson.annotations.SerializedName;

public class ChildrenData {
    @SerializedName("title")
    private String title;

    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("author")
    private String author;

    @SerializedName("selftext")
    private String subtext = "";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("preview")
    private Preview preview;

    @SerializedName("secure_media")
    private SecureMedia secureMedia;

    @SerializedName("url")
    private String url;

    @SerializedName("post_hint")
    private String posthint;

    @SerializedName("num_comments")
    private int ncomments;

    private int vote = 0;

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public String getPosthint() {
        return posthint;
    }

    public void setPosthint(String posthint) {
        this.posthint = posthint;
    }

    public int getNcomments() {
        return ncomments;
    }

    public void setNcomments(int ncomments) {
        this.ncomments = ncomments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SecureMedia getSecureMedia() {
        return secureMedia;
    }

    public void setSecureMedia(SecureMedia secureMedia) {
        this.secureMedia = secureMedia;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
}
