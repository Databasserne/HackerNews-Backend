/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
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
     * @param commentId
     * @return List of comment objects
     */
    List<Comment> getCommentsAndChildComments(int commentId);

    /**
     * Create Comment
     *
     * @param postId Id of post 
     * @param body Text of Comment
     * @return Create Comment
     */
    Comment createComment(String body, int postId);
    
    /**
     * Upvotes a Post
     * @param user User to vote
     * @param comment Comment to vote
     * @param vote vote number
     * @return The created vote
     */
    Vote voteComment(User user, Comment comment, int vote);

}
