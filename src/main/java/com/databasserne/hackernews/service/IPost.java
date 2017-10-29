package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;

import javax.ws.rs.NotFoundException;
import java.util.List;

public interface IPost {
    /**
     * Get all posts
     * @return List of Post objects.
     */
    List<Post> getAllPosts();

    /**
     * Get post
     * @param id id of the post.
     * @return Post object.
     * @throws NotFoundException If no posts where found.
     */
    Post getPost(int id) throws NotFoundException;

    /**
     * Creates post
     * @param title Title of post
     * @param body Text of post.
     * @return Post object,
     */
    Post createPost(String title, String body);
}
