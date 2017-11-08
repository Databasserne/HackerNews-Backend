package com.databasserne.hackernews.config;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;
import java.util.Map;

@WebListener
public class DeploymentCfg implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Map<String, String> env = System.getenv();
        if(env.keySet().contains("PROD")) {
            DatabaseCfg.PU_NAME = DatabaseCfg.PU_NAME_PROD;
        } else if(env.keySet().contains("TEST")) {
            DatabaseCfg.PU_NAME = DatabaseCfg.PU_NAME_TEST;
        } else {
            DatabaseCfg.PU_NAME = DatabaseCfg.PU_NAME_DEV;
        }
        //DatabaseCfg.PU_NAME = DatabaseCfg.PU_NAME_PROD;

        try {
            ServletContext context = servletContextEvent.getServletContext();
            EntityManager em = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME).createEntityManager();

            User user = new User();
            user.setUsername("TestUser");
            user.setPassword("1234");
            user.setFullname("Test User");

            User user2 = new User();
            user2.setUsername("Vixo");
            user2.setPassword("1234");
            user2.setFullname("Martin");

            Post post = new Post();
            post.setTitle("Post number 1");
            post.setBody("This is a test text for post number 1");
            post.setCreated(new Date());
            post.setUpdated(new Date());
            post.setAuthor(user);

            Post post2 = new Post();
            post2.setTitle("Post number 2");
            post2.setBody("This is a test text for second post :-)");
            post2.setCreated(new Date());
            post2.setUpdated(new Date());
            post2.setAuthor(user);

            try {
                em.getTransaction().begin();

                em.persist(user);
                em.persist(user2);
                em.persist(post);
                em.persist(post2);

                em.getTransaction().commit();
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
