/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.SimulatorPost;

/**
 *
 * @author Kasper S. Worm
 */
public interface ISimulatorRepo {

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
     * @param post post to create
     * @return created post
     */
    public SimulatorPost createPost(SimulatorPost post);

}
