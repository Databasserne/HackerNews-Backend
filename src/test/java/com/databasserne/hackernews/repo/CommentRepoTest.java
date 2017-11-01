/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.CommentRepo;
import java.util.Date;
import java.util.List;
import javax.persistence.Persistence;
import static org.hamcrest.CoreMatchers.nullValue;
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

    private static ICommentRepo commentRepo;

    @BeforeClass
    public static void setUpClass() {
        commentRepo = new CommentRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void createCommentSuccesTest() {
        Comment comment = new Comment();
        comment.setComment_text("Create comment test");
        comment.setCreated(new Date());
        comment.setPost_id(1);

        Comment result = commentRepo.createComment(comment);
        assertThat(result, is(notNullValue()));
        assertThat(result.getComment_text(), is("Create comment test"));
    }

    @Test
    public void getCommentsAndChildCommentsTest() {
        List<Comment> res = commentRepo.getCommentsAndChildComments(1);

        assertThat(res, is(notNullValue()));
        assertThat(res.size(), is(3));
    }

    @Test
    public void getChildCommentsTest() {
        List<Comment> res = commentRepo.getChildComment(1);

        assertThat(res, is(notNullValue()));
        assertThat(res.size(), is(2));
    }

    @Test
    public void getCommentSuccessTest() {
        Comment c = commentRepo.getCommentFromId(1);

        assertThat(c.getId(), is(1));
    }

    @Test
    public void getCommentFailureTest() {
        Comment c = commentRepo.getCommentFromId(999);

        assertThat(c, is(nullValue()));
    }

    /*@Test
    public void getCommentForPost() {
        List<Object[]> res = commentRepo.getCommentsForPost(2);

        assertThat(res.size(), is(4));
    }*/
}
