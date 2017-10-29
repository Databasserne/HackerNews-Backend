package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;

import java.util.List;

public interface IPost {
    /**
     * Get all posts
     * @return List of Post objects.
     */
    List<Post> getAllPosts();
}
