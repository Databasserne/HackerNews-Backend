package com.databasserne.hackernews.service;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.impl.PostRepo;
import org.junit.*;

import javax.persistence.Persistence;
import javax.ws.rs.NotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostIT {

    private IPost postService;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        postService = new PostService(new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST)));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAllPostsTest() {
        List<Post> result = postService.getAllPosts();
        assertThat(result.size(), is(2));
    }

    @Test
    public void getPostSuccessTest() {
        Post result = postService.getPost(1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1));
    }

    @Test (expected = NotFoundException.class)
    public void getPostNotFoundTest() {
        postService.getPost(500);
    }

}
