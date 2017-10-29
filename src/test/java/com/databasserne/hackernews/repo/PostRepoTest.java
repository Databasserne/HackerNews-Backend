package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.Post;
import com.databasserne.hackernews.repo.impl.PostRepo;
import com.databasserne.hackernews.repo.impl.UserRepo;
import org.junit.*;

import javax.persistence.Persistence;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    @Test
    public void getAllPostsSuccessTest() {
        List<Post> result = postRepo.getAllPosts();

        assertThat(result.size(), is(2));
    }
}
