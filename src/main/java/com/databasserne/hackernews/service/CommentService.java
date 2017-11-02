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
import com.databasserne.hackernews.repo.IPostRepo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    private IPostRepo postRepo;

    public CommentService() {
    }

    public CommentService(ICommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public CommentService(ICommentRepo commentRepo, IPostRepo postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    @Override
    public JsonArray getCommentsForPost(int id, int userId) {
        List<Object[]> comments;
        if(userId == -1) {
            comments = commentRepo.getCommentsForPost(id);
        } else {
            comments = commentRepo.getCommentsForPost(id, userId);
        }
        if(comments == null || comments.size() <= 0) return new JsonArray();
        int karma = postRepo.getUserKarma(userId);

        JsonArray array = new JsonArray();
        for(Object[] obj : comments) {
            JsonObject json = new JsonObject();
            json.addProperty("id", obj[0].toString());
            json.addProperty("text", obj[1].toString());
            json.addProperty("created_at", obj[2].toString());
            int parent_id = 0;
            if(obj[3] != null) parent_id = Integer.parseInt(obj[3].toString());
            json.addProperty("parent_id", parent_id);
            json.addProperty("author_name", obj[4].toString());
            json.addProperty("has_upvoted", Integer.parseInt(obj[5].toString()));
            json.addProperty("has_downvoted", Integer.parseInt(obj[6].toString()));
            json.addProperty("votes", Integer.parseInt(obj[7].toString()));
            json.addProperty("canDownvote", (karma > 500));

            array.add(json);
        }

        return array;
    }

    @Override
    public Comment createComment(String body, User user, int postId) {
        if(body == null || body.equals("")) throw new BadRequestException();
        
        Comment comment = new Comment();
        comment.setComment_text(body);
        Date now = new Date();
        comment.setCreated(now);
        comment.setPost_id(postId);
        comment.setAuthor(user);
        
        Comment responseComment = commentRepo.createComment(comment);
        if(responseComment == null) throw new BadRequestException();
        return responseComment;
        
    }

    @Override
    public Comment createComment(String body, User user, int postId, int commentId) {
        if(body == null || body.equals("")) throw new BadRequestException();

        Comment comment = new Comment();
        comment.setComment_text(body);
        Date now = new Date();
        comment.setCreated(now);
        comment.setPost_id(postId);
        comment.setParentCommentId(commentId);
        comment.setAuthor(user);

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
        List<Object[]> votes = commentRepo.getUserVoteForComment(user, comment);
        if(votes != null && votes.size() > 0) throw new BadRequestException("Comment already voted.");
        if(vote == -1) {
            int userKarma = postRepo.getUserKarma(user.getId());
            if(userKarma < 500) throw new BadRequestException("Not enough karma for downvote.");
        }

        Vote v = new Vote();
        v.setVote(vote);
        v.setComment(comment);
        v.setAuthor(user);
        v = commentRepo.createVote(v);
        if(v == null) throw new BadRequestException();
        return v;
    }
}
