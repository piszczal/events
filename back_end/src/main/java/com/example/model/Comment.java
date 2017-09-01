package com.example.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Szymon on 21.03.2017.
 */
@JsonAutoDetect
public class Comment {

    public Integer id;
    public String title;
    public String content;
    public Integer rating;

    public Comment() {

    }

    public Comment(Integer id, String title, String content, Integer rating) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
