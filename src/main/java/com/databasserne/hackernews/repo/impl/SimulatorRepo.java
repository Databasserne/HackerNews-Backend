/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.ISimulatorRepo;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

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
            List<Post> posts = em.createQuery("SELECT p FROM Post p")
                    .getResultList();

            return findLatestPost(posts);
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Post createPost(Post post) {
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
        return 0;
    }

}
