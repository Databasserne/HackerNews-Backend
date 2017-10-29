package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;

import javax.ws.rs.NotFoundException;
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
        Post post = postRepo.getPostById(id);
        if(post == null) throw new NotFoundException("Post not found.");

        return post;
    }

    @Override
    public Post createPost(String title, String body) {
        return null;
    }
}
