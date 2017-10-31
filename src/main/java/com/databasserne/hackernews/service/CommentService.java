/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.repo.ICommentRepo;
import java.util.Date;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author jonassimonsen
 */
public class CommentService implements IComment{
    private ICommentRepo commentRepo;

    public CommentService() {
    }

    public CommentService(ICommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    @Override
    public List<Comment> getCommentsForPost(int id) {
        List<Comment> comments = commentRepo.getCommentsForPost(id);
        if(comments == null) throw new NotFoundException("Comments not found.");
        
        return comments;
    }

    @Override
    public Comment createComment(String body, int postId) {
        if(body == null || body.equals("")) throw new BadRequestException();
        
        Comment comment = new Comment();
        comment.setComment_text(body);
        Date now = new Date();
        comment.setCreated(now);
        comment.setPost_id(postId);
        
        Comment responceComment = commentRepo.createComment(comment);
        if(responceComment == null) throw new BadRequestException();
        return responceComment;
        
    }

    @Override
    public List<Comment> getSingleCommentAndChildComment(int postId, int commentId) {
        return null;
    }
}
