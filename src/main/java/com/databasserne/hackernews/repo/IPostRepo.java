package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;

import java.util.List;

public interface IPostRepo {
    /**
     * Retreives all posts.
     * @return List of Post objects or null, if nothing is found.
     */
    List<Object[]> getAllPosts();

    /**
     * Reteives all posts with "hasDowned" and "hasUpvoted".
     * @param userId
     * @return
     */
    List<Object[]> getAllPosts(int userId);

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

    /**
     * Updated the post.
     * @param post The Post object, which should be updated.
     * @return new Post object.
     */
    Post editPost(Post post);

    /**
     * Returns a list of Posts, created by specific user.
     * @param user Author of posts.
     * @return List of Post objects or empty list, if user has not created any posts.
     */
    List<Post> getUserPosts(User user);

    /**
     * Creates a new Vote
     * @param vote Vote to create.
     * @return The created Vote object or null if not created.
     */
    Vote createVote(Vote vote);

    /**
     * Get users vote for specific Post.
     * @param user User who has voted.
     * @param post Post which is voted on.
     * @return Vote or null, if users has not voted.
     */
    List<Object[]> getUserVoteForPost(User user, Post post);


    /**
     * Retreives amount of karma for specific user.
     * @param userId
     * @return
     */
    int getUserKarma(int userId);
}
