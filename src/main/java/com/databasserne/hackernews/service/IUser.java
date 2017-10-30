package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;

public interface IUser {
    User getUserInfo(int id);
    User editUserInfo(User user);
}
