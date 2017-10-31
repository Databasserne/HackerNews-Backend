/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.repo.ICommentRepo;
import com.databasserne.hackernews.service.IComment;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
    public List<Comment> getCommentsForPost(int id) {
        em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Comment c WHERE c.post_id = :postid")
                    .setParameter("postid", id)
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
    public List<Comment> getCommentsAndChildComments(int postId, int commentId) {
        em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT * FROM comment WHERE ID = " + postId + "\n"
                    + "UNION\n"
                    + "SELECT * FROM comment WHERE PARENTCOMMENTID = " + postId + "", Comment.class)
                    .getResultList();

        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
    }
}
