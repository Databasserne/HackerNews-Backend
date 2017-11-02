package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.impl.PostRepo;
import org.junit.*;

import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

public class PostRepoTest {

    private IPostRepo postRepo;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        postRepo = new PostRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST));
    }

    @After
    public void tearDown() {
    }

    /*@Test
    public void getAllPostsSuccessTest() {
        List<Object[]> result = postRepo.getAllPosts();

        assertThat(result.size(), is(2));
    }*/

    @Test
    public void getPostSuccessTest() {
        Post p = postRepo.getPostById(1);

        assertThat(p.getId(), is(1));
        assertThat(p.getTitle(), is("Post1"));
    }

    @Test
    public void getPostNotFoundTest() {
        Post p = postRepo.getPostById(500);

        assertThat(p, is(nullValue()));
    }

    @Test
    public void createPostSuccessTest() {
        Post post = new Post();
        post.setTitle("My test post");
        post.setBody("Hej med dig, hahaha");
        post.setCreated(new Date());
        post.setUpdated(new Date());

        Post result = postRepo.createPost(post);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is("My test post"));
    }

    @Test
    public void editPostSuccessTest() {
        Post post = new Post();
        post.setTitle("Created post");
        post.setBody("haha");

        Post postToEdit = postRepo.createPost(post);
        postToEdit.setTitle("New edited post");
        postToEdit.setBody("uhhh");

        Post resultPost = postRepo.editPost(postToEdit);
        assertThat(resultPost, is(notNullValue()));
        assertThat(resultPost.getTitle(), is("New edited post"));
        assertThat(resultPost.getBody(), is("uhhh"));
    }

    @Test
    public void getUserPostsWithDataTest() {
        User user = new User();
        user.setId(1);
        List<Object[]> result = postRepo.getUserPosts(user);

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), greaterThan(1));
    }

    @Test
    public void getUserPostsWithNoDataTest() {
        User user = new User();
        user.setId(500);
        List<Object[]> result = postRepo.getUserPosts(user);

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void createVoteSuccessTest() {
        Vote vote = new Vote();
        vote.setVote(1);

        Vote result = postRepo.createVote(vote);
        assertThat(result, is(notNullValue()));
        assertThat(result.getVote(), is(1));
    }
}
