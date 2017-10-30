/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Comment;
import java.util.List;

/**
 *
 * @author jonassimonsen
 */
public interface IComment {

    /**
     *
     * @param id
     * @return List of comment objects
     */
    List<Comment> getCommentsForPost(int id);

    /**
     *
     * @param postId
     * @param userId
     * @return List of comment objects
     */
    List<Comment> getSingleCommentAndChildComment(int postId, int userId);

    /**
     * Create Comment
     *
     * @param body Text of Comment
     * @return Created Comment
     */
    Comment createComment(String body);

}
