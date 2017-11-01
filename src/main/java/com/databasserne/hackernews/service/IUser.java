package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;

import javax.ws.rs.BadRequestException;

public interface IUser {
    /**
     * Gets information about a user.
     * @param id Id of user.
     * @return User object
     */
    User getUserInfo(int id);

    /**
     * Edits a user.
     * @param user User object with edited attributes.
     * @return Updated user.
     * @throws BadRequestException if user could not be updated.
     */
    User editUserInfo(User user) throws BadRequestException;
}
