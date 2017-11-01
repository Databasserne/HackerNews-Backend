package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.IPostRepo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PostRepo implements IPostRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public PostRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Object[]> getAllPosts() {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT p.id, p.title, p.body, p.created, u.username, 0 AS hasUpvoted, " +
                    "0 AS hasDownvoted, " +
                    "(SELECT IFNULL(SUM(vote),0) FROM vote WHERE post_id = p.id) AS votes " +
                    "FROM post AS p " +
                    "JOIN User AS u ON u.ID = p.author_id")
                    .getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> getAllPosts(int userId) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT p.id, p.title, p.body, p.created, u.username, (CASE WHEN " +
                    "(EXISTS " +
                    "(SELECT v.vote FROM vote AS v WHERE author_id = ?userId " +
                    "AND v.vote = 1 " +
                    "AND v.post_id = p.id)) " +
                    "THEN 1 ELSE 0 END) AS hasUpvoted, " +
                    "(CASE WHEN " +
                    "(EXISTS " +
                    "(SELECT '' FROM vote AS v WHERE author_id = ?userId " +
                    "AND v.vote = -1 " +
                    "AND v.post_id = p.id)) " +
                    "THEN 1 ELSE 0 END) AS hasDownvoted, " +
                    "(SELECT IFNULL(SUM(vote),0) FROM vote WHERE post_id = p.id) AS votes " +
                    "FROM post AS p " +
                    "JOIN User AS u ON u.ID = p.author_id")
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (IllegalArgumentException argument) {
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
    public List<Post> getUserPosts(User user) {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Post p WHERE p.author = :author")
                    .setParameter("author", user)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
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

    @Override
    public Vote getUserVoteForPost(User user, Post post) {
        return null;
    }

    @Override
    public int getUserKarma(int userId) {
        em = emf.createEntityManager();
        try {
            BigDecimal value = (BigDecimal) em.createNativeQuery("SELECT IFNULL(SUM(v.vote),0) AS karma FROM vote AS v\n" +
                    "JOIN post AS p ON p.id = v.post_id AND p.AUTHOR_ID = ?userId")
                    .setParameter("userId", userId)
                    .getSingleResult();
            return value.intValue();
        } finally {
            em.close();
        }
    }
}
