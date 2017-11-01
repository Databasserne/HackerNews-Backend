package com.databasserne.hackernews.service;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.impl.PostRepo;
import org.junit.*;

import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

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

    /*@Test
    public void getAllPostsTest() {
        List<Post> result = postService.getAllPosts();
        assertThat(result.size(), greaterThan(1));
    }*/

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

    @Test
    public void createPostSuccessTest() {
        String title = "My Title";
        String body = "haha, test haha";

        Post result = postService.createPost(title, body);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(title));
        assertThat(result.getBody(), is(body));
    }

    @Test (expected = BadRequestException.class)
    public void createPostNoTitleTest() {
        String title = null;
        String body = "haha, test haha";

        postService.createPost(title, body);
    }

    @Test (expected = BadRequestException.class)
    public void createPostNoBodyTest() {
        String title = "My Title";
        String body = null;

        postService.createPost(title, body);
    }

    @Test (expected = BadRequestException.class)
    public void createPostEmptyTitleTest() {
        String title = "";
        String body = "haha";

        postService.createPost(title, body);
    }

    @Test (expected = BadRequestException.class)
    public void createPostEmptyBodyTest() {
        String title = "My Title";
        String body = "";

        postService.createPost(title, body);
    }
}
