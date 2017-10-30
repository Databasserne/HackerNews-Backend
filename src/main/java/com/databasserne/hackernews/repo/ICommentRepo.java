/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Comment;
import java.util.List;

/**
 *
 * @author jonassimonsen
 */
public interface ICommentRepo {

    /**
     *
     * @param id
     * @return List of comment objects
     */
    List<Comment> getCommentsForPost(int id);

    /**
     * Create Comment
     *
     * @param comment Text of Comment
     * @return Created Comment
     */
    Comment createComment(Comment comment);
    
    /**
     * 
     * @param id
     * @return 
     */
    Comment getCommentFromId(int id);
    
    
    /**
     * 
     * @param commentId
     * @return 
     */
    List<Comment> getChildComment(int commentId);
    
}

