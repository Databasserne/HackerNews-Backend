package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;

import java.util.List;

public class PostService implements IPost {

    private IPostRepo postRepo;

    public PostService() {}

    public PostService(IPostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepo.getAllPosts();
    }
}
