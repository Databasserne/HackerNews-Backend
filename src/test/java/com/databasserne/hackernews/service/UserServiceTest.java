package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.databasserne.hackernews.repo.IUserRepo;
import org.junit.*;

import javax.ws.rs.NotFoundException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    private IUser userService;
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
        userService = new UserService(userRepo);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getUserInfoSuccessTest() {
        User user = new User();

        when(userRepo.getUserById(anyInt())).thenReturn(user);

        User result = userService.getUserInfo(1);
        assertThat(result, is(notNullValue()));
    }

    @Test (expected = NotFoundException.class)
    public void getUserInfoNoUserTest() {
        when(userRepo.getUserById(anyInt())).thenReturn(null);

        userService.getUserInfo(500);
    }

    @Test
    public void editUserInfoSuccessTest() {
        User user = new User();
        user.setUsername("MyUser");
        user.setFullname("Hej alle");

        User expected = new User();
        expected.setUsername("MyUser");
        expected.setFullname("Whaaat");

        when(userRepo.editUser(user)).thenReturn(expected);

        User result = userService.editUserInfo(user);
        assertThat(result, is(notNullValue()));
        assertThat(result.getUsername(), is("MyUser"));
        assertThat(result.getFullname(), is("Whaaat"));
    }
}
