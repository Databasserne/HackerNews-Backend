package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.IPostRepo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.models.auth.In;

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
    public JsonArray getAllPosts(int userId) {
        List<Object[]> posts;
        if(userId == -1) {
            posts = postRepo.getAllPosts();
        } else {
            posts = postRepo.getAllPosts(userId);
        }
        if(posts == null) return new JsonArray();
        int karma = postRepo.getUserKarma(userId);
        boolean canDownvote = karma > 500;
        JsonArray array = new JsonArray();
        for (Object[] row : posts) {
            JsonObject json = new JsonObject();
            json.addProperty("id", Integer.parseInt(row[0].toString()));
            json.addProperty("title", row[1].toString());
            json.addProperty("body", row[2].toString());
            json.addProperty("created_at", row[3].toString());
            json.addProperty("author_name", row[4].toString());
            json.addProperty("hasUpvoted", Integer.parseInt(row[5].toString()));
            json.addProperty("hasDownvoted", Integer.parseInt(row[6].toString()));
            json.addProperty("votes", Integer.parseInt(row[7].toString()));
            json.addProperty("canDownvote", canDownvote);

            array.add(json);
        }

        return array;
    }

    @Override
    public List<Post> getUserPosts(User user) {
        return postRepo.getUserPosts(user);
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
    public Post editPost(Post post) {
        post.setUpdated(new Date());
        Post newPost = postRepo.editPost(post);
        if(newPost == null) throw new BadRequestException();

        return newPost;
    }

    @Override
    public Post deletePost(Post post) {
        if(post.getDeleted() != null) return post;

        post.setDeleted(new Date());
        post = postRepo.editPost(post);
        if(post == null) throw new BadRequestException();

        return post;
    }

    @Override
    public Vote votePost(User user, Post post, int vote) {
        if(vote != 1 && vote != -1) throw new BadRequestException("Wrong vote number.");
        post = postRepo.getPostById(post.getId());
        if(post == null) throw new NotFoundException("Post not found.");
        Vote v = postRepo.getUserVoteForPost(user, post);
        if(v != null) throw new BadRequestException("Post already voted.");

        v = new Vote();
        v.setVote(vote);
        v.setPost(post);
        v.setAuthor(user);
        v = postRepo.createVote(v);
        if(v == null) throw new BadRequestException();
        return v;
    }
}
