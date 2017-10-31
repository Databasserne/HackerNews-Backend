/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.service.IComment;
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
     * @return Specific Comment 
     */
    Comment getCommentFromId(int id);
    
    
    /**
     * 
     * @param commentId
     * @return All childComments to a Comment
     */
    List<Comment> getChildComment(int commentId);
    
    
    /**
     * 
     * @param postId
     * @param commentId
     * @return ChildComment to a Comment
     */
    List<Comment> getCommentsAndChildComments(int postId, int commentId);
}