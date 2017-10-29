package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;
import org.junit.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthenticationServiceTest {

    private IAuthentication authService;
    private IUserRepo userRepo;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        initMocks(this);
        userRepo = mock(IUserRepo.class);
        authService = new Authentication(userRepo);
    }

    @After
    public void tearDown() {
    }

    @Test(expected = NotFoundException.class)
    public void loginUserNotExistTest() {
        String username = "TestUser";
        String pass = "1234";

        when(userRepo.getUserByUsername(username)).thenReturn(null);

        authService.login(username, pass);
    }

    @Test
    public void loginSuccessTest() {
        String username = "NotExistUser";
        String pass = "1234";
        User resultUser = new User();
        resultUser.setUsername(username);
        resultUser.setPassword(pass);

        when(userRepo.getUserByUsername(username)).thenReturn(resultUser);

        User user = authService.login(username, pass);

        assertThat(user, is(notNullValue()));
        assertThat(user.getUsername(), is(username));
    }

    @Test (expected = BadRequestException.class)
    public void loginUserWithNullUsernameTest() {
        String username = null;
        String pass = "1234";

        authService.login(username, pass);
    }

    @Test (expected = BadRequestException.class)
    public void loginUserWithNullPasswordTest() {
        String username = "TestUser";
        String pass = null;

        authService.login(username, pass);
    }

    @Test (expected = BadRequestException.class)
    public void loginUserWithWrongPasswordTest() {
        String username = "TestUser";
        String pass = "wrongPass";
        User resultUser = new User();
        resultUser.setUsername(username);
        resultUser.setPassword("hejmeddig");

        when(userRepo.getUserByUsername(username)).thenReturn(resultUser);

        authService.login(username, pass);
    }

    @Test
    public void registerUserSuccessTest() {
        String username = "NewUser";
        String password = "1234";
        String rep_password = "1234";
        String fullname = "New User";

        User resultUser = authService.register(username, password, rep_password, fullname);
        assertThat(resultUser, is(notNullValue()));
        assertThat(resultUser.getUsername(), is(username));
        assertThat(resultUser.getFullname(), is(fullname));
        verify(userRepo).createUser((User)anyObject());
    }

    @Test (expected = BadRequestException.class)
    public void registerUserUsernameAlreadyTakenTest() {
        String username = "TestUser";
        String password = "1234";
        String rep_password = "1234";
        String fullname = "New User";

        authService.register(username, password, rep_password, fullname);
    }

    @Test (expected = BadRequestException.class)
    public void registerUserPasswordMismatchTest() {
        String username = "NewUser";
        String password = "1234";
        String rep_password = "1337";
        String fullname = "New User";

        authService.register(username, password, rep_password, fullname);
    }

    @Test (expected = BadRequestException.class)
    public void registerUserNoUsernameTest() {
        String username = null;
        String password = "1234";
        String rep_password = "1234";
        String fullname = "Test User";

        authService.register(username, password, rep_password, fullname);
    }

    @Test (expected = BadRequestException.class)
    public void registerUserNoPasswordTest() {
        String username = "NewUser";
        String password = null;
        String rep_password = "1234";
        String fullname = "New User";

        authService.register(username, password, rep_password, fullname);
    }
}
