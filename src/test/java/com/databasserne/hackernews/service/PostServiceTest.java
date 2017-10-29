package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;
import com.databasserne.hackernews.repo.impl.PostRepo;
import org.junit.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
        Post p1 = new Post();
        Post p2 = new Post();
        List<Post> expected = new ArrayList<>();
        expected.add(p1);
        expected.add(p2);

        when(postRepo.getAllPosts()).thenReturn(expected);

        List<Post> result = postService.getAllPosts();
        assertThat(result.size(), is(2));
    }

    @Test
    public void getAllPostsNoPostsTest() {
        when(postRepo.getAllPosts()).thenReturn(null);

        List<Post> result = postService.getAllPosts();
        assertThat(result, is(notNullValue()));
        assertThat(result, is(empty()));
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

        Post result = postService.editPost(1, title, body);
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

        Post result = postService.editPost(1, title, body);
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

        Post result = postService.editPost(1, title, body);
        assertThat(result, is(notNullValue()));
        assertThat(result.getTitle(), is(notNullValue()));
        assertThat(result.getBody(), is(body));
    }

    @Test (expected = NotFoundException.class)
    public void editPostNotFoundTest() {
        String title = "new title";
        String body = "new body";

        when(postRepo.getPostById(1)).thenReturn(null);

        postService.editPost(1, title, body);
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

        postService.editPost(1, title, body);
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

        postService.editPost(1, title, body);
    }
}
