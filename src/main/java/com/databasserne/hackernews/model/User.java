package com.databasserne.hackernews.model;

import com.databasserne.hackernews.service.security.Sha3;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Expose
    private int id;

    @Column(unique = true)
    @Expose
    private String username;

    @Column
    private String password;

    @Column
    @Expose
    private String fullname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Sha3.encode(password);
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
