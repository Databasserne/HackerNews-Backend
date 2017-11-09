/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.Harnest;

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
    public Post createPost(Post post, String username, String password);

    /**
     * Simulator create comment
     *
     * @param comment comment to create
     * @return created comment
     */
    public Comment createComment(Comment comment, String username, String password);

    public Harnest getHarnest(int id);
    
}
