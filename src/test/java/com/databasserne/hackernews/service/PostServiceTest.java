package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.IPostRepo;
import com.databasserne.hackernews.repo.impl.PostRepo;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
}
