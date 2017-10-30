package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import org.junit.*;

import javax.persistence.EntityExistsException;
import javax.persistence.Persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;

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
    public void getUserByIdSuccessTest() {
        int id = 1;
        User user = userRepo.getUserById(id);

        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(id));
    }

    @Test
    public void getUserByIdNotFoundTest() {
        int id = 500;
        User user = userRepo.getUserById(id);

        assertThat(user, is(nullValue()));
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

    @Test
    public void createUserSuccessTest() {
        User user = new User();
        user.setUsername("NewUser");
        user.setPassword("1234");
        user.setFullname("New User");

        User resultUser = userRepo.createUser(user);
        assertThat(resultUser.getUsername(), is("NewUser"));
        assertThat(resultUser.getFullname(), is("New User"));
    }

    @Test (expected = EntityExistsException.class)
    public void createUserAlreadyExistTest() {
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("1234");
        user.setFullname("Test User");

        userRepo.createUser(user);;
    }
}
