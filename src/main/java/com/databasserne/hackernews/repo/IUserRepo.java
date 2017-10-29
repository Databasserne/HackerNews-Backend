package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.User;

public interface IUserRepo {
    /**
     * Finds user with specific username
     * @param username
     * @return User or null if user doesn't exist.
     */
    User getUserByUsername(String username);

    /**
     * Creates a new user.
     * @param user User object to create.
     * @return User if successfully created.
     */
    User createUser(User user);
}
