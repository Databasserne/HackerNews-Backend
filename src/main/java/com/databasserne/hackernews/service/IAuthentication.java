package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

public interface IAuthentication {
    /**
     * Tries to login the user.
     *
     * @param username Username of user.
     * @param password Password of user.
     * @return User if validation is correct.
     * @throws BadRequestException if either email or password is null or empty or if password doesn't match user password.
     * @throws NotFoundException if user does not exist.
     */
    User login(String username, String password) throws BadRequestException, NotFoundException;
}
