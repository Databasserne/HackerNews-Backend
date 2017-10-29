package com.databasserne.hackernews.config;

import com.databasserne.hackernews.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
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

        try {
            ServletContext context = servletContextEvent.getServletContext();
            EntityManager em = Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME).createEntityManager();

            User user = new User();
            user.setUsername("TestUser");
            user.setPassword("1234");
            user.setFullname("Test User");

            try {
                em.getTransaction().begin();

                em.persist(user);

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
