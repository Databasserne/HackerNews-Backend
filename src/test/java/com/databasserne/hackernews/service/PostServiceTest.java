package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.model.Vote;
import com.databasserne.hackernews.repo.IPostRepo;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.google.gson.JsonArray;
import org.junit.*;

import javax.json.Json;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PostServiceTest {

    private IPost postService;
    private IPostRepo postRepo;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        initMocks(this);
        postRepo = mock(PostRepo.class);
        postService = new PostService(postRepo);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAllPostsTest() {
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[] {1, "Test title", "body body", "2017-08-07", "Martin", "0", "0", "5", false});
        expected.add(new Object[] {1, "Test title", "body body", "2017-08-07", "Martin", "0", "0", "5", false});

        when(postRepo.getAllPosts()).thenReturn(expected);

        JsonArray result = postService.getAllPosts(-1);
        assertThat(result.size(), is(2));
    }

    @Test
    public void getAllPostsNoPostsTest() {
        when(postRepo.getAllPosts()).thenReturn(null);

        JsonArray result = postService.getAllPosts(-1);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getUserPostsWithDataTest() {
        User user = new User();
        List<Object[]> expected = new ArrayList<>();
        expected.add(new Object[] {});
        expected.add(new Object[] {});

        when(postRepo.getUserPosts(user)).thenReturn(expected);

        JsonArray result = postService.getUserPosts(user);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }

    @Test
    public void getUserPostsWithNoDataTest() {
        User user = new User();
        List<Object[]> expected = new ArrayList<>();

        when(postRepo.getUserPosts(user)).thenReturn(expected);

        JsonArray result = postService.getUserPosts(user);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getPostSuccessTest() {
        Post p1 = new Post();
        p1.setId(1);

        when(postRepo.getPostById(anyInt())).thenReturn(p1);

        Post result = postService.getPost(1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(1));
    }

    @Test (expected = NotFoundException.class)
    public void getPostNotFoundTest() {
        when(postRepo.getPostById(anyInt())).thenReturn(null);

        postService.getPost(500);
    }

    @Test
    public void createPostSuccessTest() {
        String title = "My Title";
        String body = "haha, test haha";
        Post expected = new Post();
        expected.setTitle(title);
        expected.setBody(body);

        when(postRepo.createPost((Post)anyObject())).thenReturn(expected);

        Post result = postService.createPost(title, body, 1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(title));
        assertThat(result.getBody(), is(body));
    }

    @Test (expected = BadRequestException.class)
    public void createPostNoTitleTest() {
        String title = null;
        String body = "haha, test haha";

        postService.createPost(title, body, 1);
    }

    @Test (expected = BadRequestException.class)
    public void createPostNoBodyTest() {
        String title = "My Title";
        String body = null;

        postService.createPost(title, body, 1);
    }

    @Test (expected = BadRequestException.class)
    public void createPostEmptyTitleTest() {
        String title = "";
        String body = "haha";

        postService.createPost(title, body, 1);
    }

    @Test (expected = BadRequestException.class)
    public void createPostEmptyBodyTest() {
        String title = "My Title";
        String body = "";

        postService.createPost(title, body, 1);
    }

    @Test
    public void editPostSuccessTest() {
        String title = "new title";
        String body = "new body";
        Post expected = new Post();
        expected.setId(1);
        expected.setTitle(title);
        expected.setBody(body);
        when(postRepo.getPostById(1)).thenReturn(expected);
        when(postRepo.editPost((Post)anyObject())).thenReturn(expected);

        Post result = postService.editPost(expected);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(title));
        assertThat(result.getBody(), is(body));
    }

    @Test
    public void editPostTitleTest() {
        String title = "new title";
        String body = null;
        Post expected = new Post();
        expected.setId(1);
        expected.setTitle(title);
        expected.setBody("old body");
        when(postRepo.getPostById(1)).thenReturn(expected);
        when(postRepo.editPost((Post)anyObject())).thenReturn(expected);

        Post result = postService.editPost(expected);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(title));
        assertThat(result.getBody(), is(notNullValue()));
    }

    @Test
    public void editPostBodyTest() {
        String title = null;
        String body = "new body";
        Post expected = new Post();
        expected.setId(1);
        expected.setTitle("old title");
        expected.setBody(body);
        when(postRepo.getPostById(1)).thenReturn(expected);
        when(postRepo.editPost((Post)anyObject())).thenReturn(expected);

        Post result = postService.editPost(expected);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(notNullValue()));
        assertThat(result.getBody(), is(body));
    }

    @Test (expected = BadRequestException.class)
    public void editPostNotFoundTest() {
        String title = "new title";
        String body = "new body";
        Post p = new Post();
        p.setId(1);
        p.setTitle(title);
        p.setBody(body);

        when(postRepo.getPostById(1)).thenReturn(null);

        postService.editPost(p);
    }

    @Test (expected = BadRequestException.class)
    public void editPostEmptyTitleTest() {
        String title = "";
        String body = "hej";
        Post p = new Post();
        p.setId(1);
        p.setTitle("Hej med dig");
        p.setBody("wuhuu");

        when(postRepo.getPostById(1)).thenReturn(p);

        postService.editPost(p);
    }

    @Test (expected = BadRequestException.class)
    public void editPostEmptyBodyTest() {
        String title = "new title";
        String body = "";
        Post p = new Post();
        p.setId(1);
        p.setTitle("Hej med dig");
        p.setBody("wuhuu");

        when(postRepo.getPostById(1)).thenReturn(p);

        postService.editPost(p);
    }

    @Test
    public void deletePostSuccessTest() {
        Post post = new Post();
        post.setId(500);
        post.setTitle("Hej");
        post.setBody("haha");
        post.setCreated(new Date());
        post.setUpdated(new Date());

        Post expected = post;
        expected.setDeleted(new Date());

        when(postRepo.editPost(post)).thenReturn(expected);

        Post result = postService.deletePost(post);
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(post.getId()));
        assertThat(result.getDeleted(), is(notNullValue()));
    }

    @Test
    public void deletePostAlreadyDeletedTest() {
        Post post = new Post();
        post.setId(500);
        post.setTitle("Hello");
        post.setBody("haha");
        post.setCreated(new Date());
        post.setUpdated(new Date());
        post.setDeleted(new Date());

        Post result = postService.deletePost(post);
        assertThat(result, is(notNullValue()));
        assertThat(result.getDeleted(), is(post.getDeleted()));
    }

    @Test
    public void votePostUpSuccessTest() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("Hej");
        post.setBody("Wuuu");
        User user = new User();

        Vote v = new Vote();
        v.setVote(1);

        when(postRepo.getPostById(anyInt())).thenReturn(post);
        when(postRepo.createVote((Vote)anyObject())).thenReturn(v);

        Vote result = postService.votePost(user, post, 1);

        assertThat(result, is(notNullValue()));
        assertThat(result.getVote(), is(1));
    }

    @Test
    public void votePostDownSuccessTest() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("hej");
        post.setBody("haha");
        User user = new User();

        Vote v = new Vote();
        v.setVote(-1);

        when(postRepo.getPostById(anyInt())).thenReturn(post);
        when(postRepo.getUserVoteForPost((User)anyObject(), (Post)anyObject())).thenReturn(null);
        when(postRepo.createVote((Vote)anyObject())).thenReturn(v);

        Vote result = postService.votePost(user, post, -1);
        assertThat(result, is(notNullValue()));
        assertThat(result.getVote(), is(-1));
    }

    @Test (expected = NotFoundException.class)
    public void votePostNotFoundTest() {
        Post p = new Post();
        User u = new User();

        when(postRepo.getUserVoteForPost((User)anyObject(), (Post)anyObject())).thenReturn(null);
        when(postRepo.createVote((Vote)anyObject())).thenReturn(null);

        postService.votePost(u, p, 1);
    }

    @Test (expected = BadRequestException.class)
    public void votePostAlreadyVotedTest() {
        Post p = new Post();
        List<Object[]> v = new ArrayList<>();
        User u = new User();

        when(postRepo.getPostById(anyInt())).thenReturn(p);
        when(postRepo.getUserVoteForPost((User)anyObject(), (Post)anyObject())).thenReturn(v);

        postService.votePost(u, p, 1);
    }

    @Test (expected = BadRequestException.class)
    public void votePostWrongVoteNumberTest() {
        Post p = new Post();
        User u = new User();

        postService.votePost(u, p, 5);
    }
}
