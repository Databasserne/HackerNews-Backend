/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.Harnest;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.ISimulatorRepo;
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
        return "Alive";
    }

    @Override
    public int getLatest() {
        em = emf.createEntityManager();
        try {
            return (int) em.createNativeQuery("SELECT id FROM harnest ORDER BY ID DESC LIMIT 1;").getSingleResult();
            //List<Post> posts = em.createNativeQuery("SELECT id FROM harnest ORDER BY ID DESC LIMIT 1;", Post.class).getResultList();

//            return findLatestPost(posts);
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Post createPost(Post post, String username, String password, int harnestId) {
        User user = null;
        try {
            user = simulatorLogin(username, password);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }

        try {
            post.setAuthor(user);

        } catch (Exception e) {
            System.out.println("Something is wrong...");
        }

        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(post);

            Harnest harn = new Harnest();
            harn.setId(harnestId);
            harn.setPost(post);
            em.persist(harn);

            em.getTransaction().commit();
            return post;
        } catch (EntityExistsException | RollbackException exist) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Harnest getHarnest(int id) {
        em = emf.createEntityManager();

        return em.find(Harnest.class, id);
    }

    @Override
    public Comment createComment(Comment comment, String username, String password, int harnestId) {
        User user;
        try {
            user = simulatorLogin(username, password);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        }
        comment.setAuthor(user);

        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);

            Harnest harn = new Harnest();
            harn.setId(harnestId);
            harn.setComment(comment);
            em.persist(harn);

            em.getTransaction().commit();

            return comment;
        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
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

    /**
     * Simulator login call
     *
     * @param username username
     * @param password password
     * @return User if found
     */
    private User simulatorLogin(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException();
        }
        if (password == null || password.isEmpty()) {
            throw new BadRequestException();
        }
        User user = null;
        em = emf.createEntityManager();
        try {
            user = (User) em.createQuery("SELECT u FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new NotFoundException();
        } finally {
            em.close();
        }

        if (user == null) {
            throw new NotFoundException();
        }

        if (!password.equals(user.getPassword())) {
            throw new BadRequestException();
        }

        return user;

    }

}
