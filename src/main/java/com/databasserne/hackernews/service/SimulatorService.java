/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.ISimulatorRepo;

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
        return 0;
    }

    @Override
    public Post createPost(Post post) {
        return null;
    }

}
