package com.databasserne.hackernews.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date updated;

    @JoinColumn
    @ManyToOne
    private User author;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public boolean validate(int userId) {
        if(this.author == null) return false;
        return this.author.getId() == userId;
    }
}
