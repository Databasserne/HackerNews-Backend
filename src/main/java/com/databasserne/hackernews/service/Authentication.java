package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;
import com.databasserne.hackernews.service.security.Sha3;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

public class Authentication implements IAuthentication {

    private IUserRepo userRepo;

    public Authentication() {}

    public Authentication(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User login(String username, String password) {
        if(username == null || username.isEmpty()) throw new BadRequestException();
        if(password == null || password.isEmpty()) throw new BadRequestException();

        User user = userRepo.getUserByUsername(username);
        if(user == null) throw new NotFoundException();

        if(!user.getPassword().equals(Sha3.encode(password))) throw new BadRequestException();

        return user;
    }
}
