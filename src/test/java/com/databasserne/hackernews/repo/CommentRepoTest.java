/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Comment;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
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

//    @Test
//    public void createCommentSuccesTest() {
//        Comment comment = new Comment();
//        comment.setComment_text("Hej med dig");
//        comment.setCreated(new Date());
//        comment.setPost_id(1);
//             
//        Comment result = commentRepo.createComment(comment);
//        comment.setComment_text("Comment");
//        commentRepo.createComment(comment);
//        assertThat(result, is(notNullValue()));
//        assertThat(result.getComment_text(), is("Hej med dig")); 
//    }
    
    
    @Test
    public void getComment() {
        Comment comment = new Comment();
        comment.setComment_text("Comment1");
        comment.setCreated(new Date());
        comment.setPost_id(1);
        
        Comment comment2 = new Comment();
        comment2.setComment_text("Comment2");
        comment2.setCreated(new Date());
        comment2.setPost_id(1);
        comment2.setParentCommentId(1);
        
        Comment comment3 = new Comment();
        comment3.setComment_text("Comment3");
        comment3.setCreated(new Date());
        comment3.setPost_id(1);
        comment3.setParentCommentId(1);
        
        Comment comment4 = new Comment();
        comment4.setComment_text("Comment4");
        comment4.setCreated(new Date());
        comment4.setPost_id(1);
        comment4.setParentCommentId(2);
        
        Comment comment5 = new Comment();
        comment5.setComment_text("Comment5");
        comment5.setCreated(new Date());
        comment5.setPost_id(1);
       
        Comment comment6 = new Comment();
        comment6.setComment_text("Comment6");
        comment6.setCreated(new Date());
        comment6.setPost_id(1);
        
        Comment comment7 = new Comment();
        comment7.setComment_text("Comment7");
        comment7.setCreated(new Date());
        comment7.setPost_id(1);
        
        Comment comment8 = new Comment();
        comment8.setComment_text("Comment8");
        comment8.setCreated(new Date());
        comment8.setPost_id(1);
        
        Comment comment9 = new Comment();
        comment9.setComment_text("Comment9");
        comment9.setCreated(new Date());
        comment9.setPost_id(1);
             
        Comment result = commentRepo.createComment(comment);
        commentRepo.createComment(comment2);
        commentRepo.createComment(comment3);
        commentRepo.createComment(comment4);
        commentRepo.createComment(comment5);
        commentRepo.createComment(comment6);
        commentRepo.createComment(comment7);
        commentRepo.createComment(comment8);
        commentRepo.createComment(comment9);
        assertThat(result, is(notNullValue()));
        assertThat(result.getComment_text(), is("Comment1")); 
    }
    
//    @Test
//    public void getCommentSuccesTest() {
//        Comment c = commentRepo.getCommentFromId(1);
//
//        assertThat(c.getId(), is(1));
//    }
}
