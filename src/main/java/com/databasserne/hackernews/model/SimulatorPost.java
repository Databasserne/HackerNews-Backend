/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.model;

import java.io.Serializable;

/**
 *
 * @author Kasper S. Worm
 */
public class SimulatorPost implements Serializable {

    private int id;
    private String username;
    private String post_text;
    private String post_type;
    private String post_url;
    private String post_title;
    private String pwd_hash;
    private int hanesst_id;
    private int post_parent;

    public SimulatorPost(int id, String username, String post_text, String post_type, String post_url, String post_title, String pwd_hash, int hanesst_id, int post_parent) {
        this.id = id;
        this.username = username;
        this.post_text = post_text;
        this.post_type = post_type;
        this.post_url = post_url;
        this.post_title = post_title;
        this.pwd_hash = pwd_hash;
        this.hanesst_id = hanesst_id;
        this.post_parent = post_parent;
    }

    public SimulatorPost() {
    }

    public int getPost_parent() {
        return post_parent;
    }

    public void setPost_parent(int post_parent) {
        this.post_parent = post_parent;
    }

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

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPwd_hash() {
        return pwd_hash;
    }

    public void setPwd_hash(String pwd_hash) {
        this.pwd_hash = pwd_hash;
    }

    public int getHanesst_id() {
        return hanesst_id;
    }

    public void setHanesst_id(int hanesst_id) {
        this.hanesst_id = hanesst_id;
    }

}
