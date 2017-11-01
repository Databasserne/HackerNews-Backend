package com.databasserne.hackernews.service;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.impl.UserRepo;
import org.junit.*;

import javax.persistence.Persistence;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.isNull;

public class AuthenticationServiceIT {

    private IAuthentication authService;

    public AuthenticationServiceIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        authService = new Authentication(new UserRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST)));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void loginSuccessTest() {
        String username = "TestUser";
        String password = "1234";
        User user = authService.login(username, password);

        assertThat(user, is(notNullValue()));
        assertThat(user, hasProperty("username", is(username)));
    }

    @Test(expected = NotFoundException.class)
    public void loginUserNotExistTest() {
        String username = "hej";
        String password = "7895";
        User user = authService.login(username, password);

        assertThat(user, is(isNull()));
    }

    @Test (expected = BadRequestException.class)
    public void loginUserWithNullEmailTest() {
        String email = null;
        String pass = "1234";

        authService.login(email, pass);
    }

    @Test (expected = BadRequestException.class)
    public void loginUserWithNullPasswordTest() {
        String username = "TestUser";
        String pass = null;

        authService.login(username, pass);
    }

    @Test(expected = BadRequestException.class)
    public void loginUserWrongPasswordTest() {
        String username = "TestUser";
        String password = "hahawuhuu";
        authService.login(username, password);
    }
}
