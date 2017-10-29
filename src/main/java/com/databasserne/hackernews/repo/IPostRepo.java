package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Post;

import java.util.List;

public interface IPostRepo {
    /**
     * Retreives all posts.
     * @return List of Post objects or null, if nothing is found.
     */
    List<Post> getAllPosts();
}
