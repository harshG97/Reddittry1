package com.example.reddittry1.models;

public class Vote {
    private String dir;
    private String name;
    private String rank;

    public Vote(String dir, String name, String rank){
        this.dir=dir;
        this.name = name;
        this.rank = rank;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
