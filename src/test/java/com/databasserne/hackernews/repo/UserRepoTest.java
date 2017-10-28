package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import com.databasserne.hackernews.service.AuthenticationService;
import org.junit.*;

import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserRepoTest {

    private IUserRepo userRepo;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        userRepo = new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getUserByUsernameSuccessTest() {
        String username = "TestUser";
        User user = userRepo.getUserByUsername(username);

        assertThat(user, hasProperty("username", is(username)));
    }

    @Test
    public void getUserByUsernameNotExistTest() {
        String username = "wuhuu";
        User user = userRepo.getUserByUsername(username);

        assertThat(user, is(nullValue()));
    }
}
