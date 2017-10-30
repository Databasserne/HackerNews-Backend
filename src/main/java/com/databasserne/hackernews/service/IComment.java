/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import java.util.List;

/**
 *
 * @author Kasper S. Worm
 */
public interface IComment {

    /**
     *
     * @param id
     * @return List of comment objects
     */
    List<IComment> getCommentsForPost(int id);

    /**
     *
     * @param postId
     * @param userId
     * @return List of comment objects
     */
    List<IComment> getSingleCommentAndChildComment(int postId, int userId);

    /**
     * Create Comment
     *
     * @param body Text of Comment
     * @return Created Comment
     */
    IComment createComment(String body);

}
