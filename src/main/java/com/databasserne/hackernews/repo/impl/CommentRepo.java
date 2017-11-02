/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.ICommentRepo;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

/**
 *
 * @author jonassimonsen
 */
public class CommentRepo implements ICommentRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public CommentRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Object[]> getCommentsForPost(int id, int userId) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT c.id, c.comment_text, c.created, c.parentcommentid, u.username, (CASE WHEN " +
                    "(EXISTS " +
                    "(SELECT v.vote FROM vote AS v WHERE author_id = ?userId " +
                    "AND v.vote = 1 " +
                    "AND v.comment_id = c.id)) " +
                    "THEN 1 ELSE 0 END) AS hasUpvoted, " +
                    "(CASE WHEN " +
                    "(EXISTS " +
                    "(SELECT '' FROM vote AS v WHERE author_id = ?userId " +
                    "AND v.vote = -1 " +
                    "AND v.comment_id = c.id)) " +
                    "THEN 1 ELSE 0 END) AS hasDownvoted, " +
                    "(SELECT IFNULL(SUM(vote), 0) FROM vote WHERE comment_id = c.id) AS votes " +
                    "FROM comment AS c " +
                    "JOIN user AS u ON u.ID = c.author_id " +
                    "WHERE c.post_id = ?postId")
                    .setParameter("userId", userId)
                    .setParameter("postId", id)
                    .getResultList();

        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> getCommentsForPost(int id) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT c.id, c.comment_text, c.created, c.parentcommentid, u.username, " +
                    "0 AS hasUpvoted, " +
                    "0 AS hasDownvoted, " +
                    "(SELECT IFNULL(SUM(vote), 0) FROM vote WHERE comment_id = c.id) AS votes " +
                    "FROM comment AS c " +
                    "JOIN user AS u ON u.ID = c.author_id " +
                    "WHERE c.post_id = ?postId")
                    .setParameter("postId", id)
                    .getResultList();

        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Comment getCommentFromId(int id) {
        em = emf.createEntityManager();
        try {

            return (Comment) em.find(Comment.class, id);

        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Comment> getChildComment(int commentId) {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c WHERE c.parentCommentId = :parentCommentId")
                    .setParameter("parentCommentId", commentId)
                    .getResultList();
        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Comment createComment(Comment comment) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();

            return comment;

        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Comment> getCommentsAndChildComments(int commentId) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT * FROM comment WHERE ID = " + commentId + "\n"
                    + "UNION\n"
                    + "SELECT * FROM comment WHERE PARENTCOMMENTID = " + commentId + "", Comment.class)
                    .getResultList();

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
    
    @Override
    public List<Object[]> getUserVoteForComment(User user, Comment comment) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT * FROM vote AS v " +
                    "WHERE v.author_id = ?userId AND v.comment_id = ?commentId")
                    .setParameter("userId", user.getId())
                    .setParameter("commentId", comment.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
