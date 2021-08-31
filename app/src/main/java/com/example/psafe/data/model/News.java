package com.example.psafe.data.model;

import java.util.Map;

public class News {
    private String id;
    private String title;
    private String content;
    private int like;
    private int dislike;
    private String source;
    private String image = "image";

    public News(String id, String title, String content, int like, int dislike, String source,String image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.like = like;
        this.dislike = dislike;
        this.source = source;
        this.image = image;
    }
    public News(String title) {
        this.id = "id";
        this.title = title;
        this.content = "content";
        this.like = -11;
        this.dislike = -11;
        this.source = "source";
        this.image = "image";
    }

    public News() {
        this.id = "id";
        this.title = "title";
        this.content = "content";
        this.like = -11;
        this.dislike = -11;
        this.source = "source";
        this.image = "image";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
