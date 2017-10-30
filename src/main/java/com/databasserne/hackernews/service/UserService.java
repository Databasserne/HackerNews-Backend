package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;

public class UserService implements IUser {

    private IUserRepo userRepo;

    public UserService() {}

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User getUserInfo(int id) {
        return null;
    }
}
