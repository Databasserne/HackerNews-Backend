package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;

import java.util.ArrayList;
import java.util.List;

public class PostService implements IPost {

    private IPostRepo postRepo;

    public PostService() {}

    public PostService(IPostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepo.getAllPosts();
        if(posts == null) posts = new ArrayList<>();

        return posts;
    }

    @Override
    public Post getPost(int id) {
        return null;
    }
}
