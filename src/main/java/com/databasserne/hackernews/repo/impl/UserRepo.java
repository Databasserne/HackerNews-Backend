package com.databasserne.hackernews.repo.impl;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;

import javax.persistence.*;

public class UserRepo implements IUserRepo {

    private EntityManagerFactory emf;
    private EntityManager em;

    public UserRepo() {}

    public UserRepo(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User getUserById(int id) {
        em = emf.createEntityManager();
        try {
            return (User) em.find(User.class, id);
        } catch (IllegalArgumentException argument) {
            return null;
        } finally {
            em.close();
        }
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

    @Override
    public User createUser(User user) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            return user;
        } catch (EntityExistsException exist) {
            throw new EntityExistsException();
        } catch (RollbackException rollback) {
            throw new EntityExistsException();
        } finally {
            em.close();
        }
    }

    @Override
    public User editUser(User user) {
        return null;
    }
}
