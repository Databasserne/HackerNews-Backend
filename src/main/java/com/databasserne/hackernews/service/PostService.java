package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Date;
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
        if(title == null || title.equals("")) throw new BadRequestException();
        if(body == null || body.equals("")) throw new BadRequestException();

        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        Date now = new Date();
        post.setCreated(now);
        post.setUpdated(now);

        Post responsePost = postRepo.createPost(post);
        if(responsePost == null) throw new BadRequestException();
        return responsePost;
    }

    @Override
    public Post editPost(int id, String title, String body) {
        Post post = postRepo.getPostById(id);
        if(post == null) throw new NotFoundException("Post not found.");

        if(title != null) {
            if(title.equals("")) throw new BadRequestException("Title cannot be empty.");
            post.setTitle(title);
        }
        if(body != null) {
            if(body.equals("")) throw new BadRequestException("Body cannot be empty.");
            post.setBody(body);
        }

        post.setUpdated(new Date());
        Post newPost = postRepo.editPost(post);
        if(newPost == null) throw new BadRequestException();

        return newPost;
    }
}
