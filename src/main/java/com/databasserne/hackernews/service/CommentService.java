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
        
        Comment responseComment = commentRepo.createComment(comment);
        if(responseComment == null) throw new BadRequestException();
        return responseComment;
        
    }

    @Override
    public List<Comment> getCommentsAndChildComments(int commentId) {
        List<Comment> comments = commentRepo.getCommentsAndChildComments(commentId);
        if(comments == null){
            throw new NotFoundException("No comments found.");
        }
        return comments;
    }
    
    @Override
    public Vote voteComment(User user, Comment comment, int vote) {
        if(vote != 1 && vote != -1) throw new BadRequestException("Wrong vote number.");
        comment = commentRepo.getCommentFromId(comment.getId());
        if(comment == null) throw new NotFoundException("Comment not found.");
        Vote v = commentRepo.getUserVoteForComment(user, comment);
        if(v != null) throw new BadRequestException("Comment already voted.");

        v = new Vote();
        v.setVote(vote);
        v.setComment(comment);
        v.setAuthor(user);
        v = commentRepo.createVote(v);
        if(v == null) throw new BadRequestException();
        return v;
    }
}
