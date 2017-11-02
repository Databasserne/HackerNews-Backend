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
                    "JOIN user AS u ON u.ID = p.author_id " +
                    "WHERE p.deleted IS NULL")
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
                    "JOIN user AS u ON u.ID = p.author_id " +
                    "WHERE p.deleted IS NULL")
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
    public List<Object[]> getUserPosts(User user) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT p.id, p.title, p.body, p.created, u.username, " +
                    "(SELECT IFNULL(SUM(vote),0) FROM vote WHERE post_id = p.id) AS votes " +
                    "FROM post AS p " +
                    "JOIN user AS u ON u.ID = p.author_id " +
                    "WHERE p.deleted IS NULL AND p.author_id = ?userId")
                    .setParameter("userId", user.getId())
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
    public List<Object[]> getUserVoteForPost(User user, Post post) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT * FROM vote AS v " +
                    "WHERE v.author_id = ?userId AND v.post_id = ?postId")
                    .setParameter("userId", user.getId())
                    .setParameter("postId", post.getId())
                    .getResultList();
        } finally {
            em.close();
        }
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
        } catch (Exception e) {
            return 0;
        }
        finally {
            em.close();
        }
    }
}
