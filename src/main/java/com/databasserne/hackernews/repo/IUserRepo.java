package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.model.User;

public interface IUserRepo {

    /**
     * Finds user from specific Id.
     * @param id Id of user.
     * @return User object or null if nothing is found.
     */
    User getUserById(int id);

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
