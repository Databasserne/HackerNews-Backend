/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.SimulatorPost;

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
     * @param text text of post
     * @param url url of post
     * @param username username of post owner
     * @param password password hash of post owner
     * @param type post type
     * @param hanesstId post hanesstid
     * @param parentId post parent id
     * @return the created post
     */
    public SimulatorPost simulatorPost(String title, String text, String url, String username, String password, String type, int hanesstId, int parentId);
}
