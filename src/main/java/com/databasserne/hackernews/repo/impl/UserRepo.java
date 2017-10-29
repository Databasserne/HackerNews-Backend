package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

public class UserRepo implements IUserRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public UserRepo() {}

    public UserRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User getUserByUsername(String username) {
        em = emf.createEntityManager();
        try {
            return (User) em.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
