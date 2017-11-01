/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.SimulatorPost;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.ISimulatorRepo;
import com.databasserne.hackernews.service.security.Sha3;
import java.util.Date;
import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author Kasper S. Worm
 */
public class SimulatorService implements ISimulator {

    private ISimulatorRepo simulatorRepo;

    public SimulatorService() {
    }

    public SimulatorService(ISimulatorRepo simulatorRepo) {
        this.simulatorRepo = simulatorRepo;
    }

    @Override
    public String getStatus() {
        return "ITS ALIVE!!!";
    }

    @Override
    public int getLatest() {
        return simulatorRepo.getLatest();
    }

    @Override
    public SimulatorPost simulatorPost(String title, String text, String url, String username, String password, String type, int hanesstId, int parentId) {
        if (title == null || title.equals("")) {
            throw new BadRequestException();
        }
        if (text == null || text.equals("")) {
            throw new BadRequestException();
        }

        //Create Post or Comment and save to DB
        if ("story".equals(type)) {
            System.out.println("Making post");
            Post post = new Post();
            post.setBody(text);
            Date now = new Date();
            post.setCreated(now);
            post.setUpdated(now);
            post.setTitle(title);
            simulatorRepo.createPost(post, username, password);
        } else {
            System.out.println("Making comment");
            Comment comment = new Comment();
            comment.setComment_text(text);
            Date now = new Date();
            comment.setCreated(now);
            comment.setParentCommentId(parentId);
            comment.setPost_id(hanesstId);
            simulatorRepo.createComment(comment, username, password);
        }

        //Return all content we got.
        SimulatorPost simpost = new SimulatorPost();
        simpost.setPost_text(text);
        simpost.setPost_title(title);
        simpost.setHanesst_id(hanesstId);
        simpost.setPost_parent(parentId);
        simpost.setPost_type(type);
        simpost.setPost_url(url);
        simpost.setUsername(username);
        simpost.setPwd_hash(password);
        if (simpost == null) {
            throw new BadRequestException();
        }
        return simpost;
    }

}