/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;

/**
 *
 * @author Kasper S. Worm
 */
public interface ISimulator {

    /**
     * Get status for the server
     *
     * @return Alive / Update / Down
     */
    public String getStatus();

    /**
     * Get latest post
     *
     * @return last post id
     */
    public int getLatest();

    /**
     * Simulator create post
     *
     * @param title title of post
     * @param body body of post
     * @return the created post
     */
    public Post createPost(String title, String body);
}
