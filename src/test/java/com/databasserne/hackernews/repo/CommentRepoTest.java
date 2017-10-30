/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.repo.impl.CommentRepo;
import java.util.Date;
import javax.persistence.Persistence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jonassimonsen
 */
public class CommentRepoTest {
    
    private ICommentRepo commentRepo;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        commentRepo = new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void createCommentSuccesTest() {
        Comment comment = new Comment();
        comment.setComment_text("My test comment");
        comment.setCreated(new Date());
        
        Comment result = commentRepo.createComment(comment);
        assertThat(result, is(notNullValue()));
        assertThat(result.getComment_text(), is("My test comment")); 
    }
    
    @Test
    public void getCommentSuccesTest() {
        Comment c = commentRepo.getCommentFromId(1);

        assertThat(c.getId(), is(1));
    }
}
