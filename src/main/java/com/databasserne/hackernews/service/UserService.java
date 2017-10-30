package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;

import javax.ws.rs.NotFoundException;

public class UserService implements IUser {

    private IUserRepo userRepo;

    public UserService() {}

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User getUserInfo(int id) {
        User user = userRepo.getUserById(id);
        if(user == null) throw new NotFoundException("User not found.");

        return user;
    }
}
