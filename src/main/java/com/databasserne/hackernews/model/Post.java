package com.databasserne.hackernews.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "post")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Expose
    private int id;

    @Column
    @Expose
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    @Expose
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
