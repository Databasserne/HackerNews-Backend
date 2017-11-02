package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;
import com.databasserne.hackernews.service.security.Sha3;

import javax.persistence.EntityExistsException;
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

    @Override
    public User register(String username, String password, String rep_password, String fullname) {
        if(username == null || username.isEmpty()) throw new BadRequestException("Username is required.");
        if(password == null || password.isEmpty()) throw new BadRequestException("Password is required.");
        if(!rep_password.equals(password)) throw new BadRequestException("Passwords does not match.");

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            return userRepo.createUser(user);
        } catch (EntityExistsException exist) {
            throw new BadRequestException("Username already in use.");
        }
    }
}
