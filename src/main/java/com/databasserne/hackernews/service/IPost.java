package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;

import javax.ws.rs.BadRequestException;
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

    /**
     * Updates a Post
     * @param post New Post with updated fields to update.
     * @return New updated Post.
     * @throws NotFoundException If Post could not be found.
     * @throws BadRequestException If parameters is empty strings or entity could not be updated.
     */
    Post editPost(Post post) throws NotFoundException, BadRequestException;

    /**
     * Deletes Post.
     * @param post Post to delete.
     * @return True if successfully deleted or false if error.
     */
    Post deletePost(Post post);
}
