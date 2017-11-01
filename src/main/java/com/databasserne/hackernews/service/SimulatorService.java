/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.ISimulatorRepo;
import java.util.Date;
import javax.ws.rs.BadRequestException;

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
        return null;
    }

    @Override
    public int getLatest() {
        return simulatorRepo.getLatest();
    }

    @Override
    public Post createPost(String title, String body) {
        if (title == null || title.equals("")) {
            throw new BadRequestException();
        }
        if (body == null || body.equals("")) {
            throw new BadRequestException();
        }

        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        Date now = new Date();
        post.setCreated(now);
        post.setUpdated(now);

        Post responsePost = simulatorRepo.createPost(post);
        if (responsePost == null) {
            throw new BadRequestException();
        }
        return responsePost;
    }

}
