package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Post;

import java.util.List;

public interface IPostRepo {
    List<Post> getAllPosts();
}
