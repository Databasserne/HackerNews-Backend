/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.SimulatorPost;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.ISimulatorRepo;
import com.databasserne.hackernews.service.security.Sha3;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

/**
 *
 * @author Kasper S. Worm
 */
public class SimulatorRepo implements ISimulatorRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public SimulatorRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public String getStatus() {
        return "ITS ALIVE!!!";
    }

    @Override
    public int getLatest() {
        em = emf.createEntityManager();
        try {
            List<Post> posts = em.createNativeQuery("SELECT * FROM post", Post.class).getResultList();

            return findLatestPost(posts);
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public SimulatorPost createPost(SimulatorPost post) {
        //TODO login - get authorId

        //Check type - story/comment/w.e
        //Post it
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(post);
            em.getTransaction().commit();

            return post;
        } catch (EntityExistsException | RollbackException exist) {
            return null;
        } finally {
            em.close();
        }
    }

    private boolean checkSimulatorLogin(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException();
        }
        if (password == null || password.isEmpty()) {
            throw new BadRequestException();
        }

//        User user = userRepo.getUserByUsername(username);
        User user = null;
        if (user == null) {
            throw new NotFoundException();
        }

        if (!user.getPassword().equals(Sha3.encode(password))) {
            throw new BadRequestException();
        }

        em = emf.createEntityManager();
        try {
            em.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }

//        return false;
    }

    /**
     * Check all posts and compare the id of them
     *
     * @param posts Posts to check
     * @return Largest(latest) id
     */
    private int findLatestPost(List<Post> posts) {
        int latest = 0;
        for (Post post : posts) {
            if (post.getId() > latest) {
                latest = post.getId();
            }
        }
        return latest;
    }

}