package com.example.psafe.data.model;

import java.util.Map;

public class News {
    private String id;
    private String title;
    private String content;
    private int like;
    private int dislike;
    private String source;

    public News(String id, String title, String content, int like, int dislike, String source) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.like = like;
        this.dislike = dislike;
        this.source = source;
    }

    public News() {
        this.id = "id";
        this.title = "title";
        this.content = "content";
        this.like = -11;
        this.dislike = -11;
        this.source = "source";
    }
    public News(String id, Map<String, Object> map) {
        this.id = id;
        this.title = map.get("title").toString();
        this.content = map.get("content").toString();
        this.like = Integer.parseInt(map.get("like").toString());
        this.dislike = Integer.parseInt(map.get("dislike").toString());
        this.source = map.get("source").toString();
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
