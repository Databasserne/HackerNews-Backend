package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Post;

import java.util.List;

public interface IPostRepo {
    /**
     * Retreives all posts.
     * @return List of Post objects or null, if nothing is found.
     */
    List<Post> getAllPosts();

    /**
     * Get single Post from id
     * @param id Id of specific post.
     * @return Post object.
     */
    Post getPostById(int id);

    /**
     * Creates a new Post
     * @param post Post object to create.
     * @return The created Post object.
     */
    Post createPost(Post post);
}
