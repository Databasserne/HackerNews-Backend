/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
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
    List<Object[]> getCommentsForPost(int id, int userId);

    /**
     *
     * @param id
     * @return
     */
    List<Object[]> getCommentsForPost(int id);

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
     * @param commentId
     * @return ChildComment to a Comment
     */
    List<Comment> getCommentsAndChildComments(int commentId);
    
    /**
     * Creates a new Vote
     * @param vote Vote to create.
     * @return The created Vote object or null if not created.
     */
    Vote createVote(Vote vote);
    
    /**
     * Get users vote for specific Post.
     * @param user User who has voted.
     * @param comment Post which is voted on.
     * @return Vote or null, if users has not voted.
     */
    List<Object[]> getUserVoteForComment(User user, Comment comment);
}
