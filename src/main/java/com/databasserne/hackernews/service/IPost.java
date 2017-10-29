package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;

import java.util.List;

public interface IPost {
    List<Post> getAllPosts();
}
