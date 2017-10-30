package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.IPostRepo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import java.util.List;

public class PostRepo implements IPostRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public PostRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Post> getAllPosts() {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p")
                    .getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Post getPostById(int id) {
        em = emf.createEntityManager();
        try {
            return (Post) em.find(Post.class, id);
        } catch (IllegalArgumentException argument) {
            return null;
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
        } catch (EntityExistsException exist) {
            return null;
        } catch (RollbackException rollback) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Post editPost(Post post) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(post);
            em.getTransaction().commit();

            return post;
        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Vote createVote(Vote vote) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vote);
            em.getTransaction().commit();

            return vote;
        } catch (EntityExistsException exist) {
            return null;
        } catch (RollbackException rollback) {
            return null;
        } finally {
            em.close();
        }
    }
}
