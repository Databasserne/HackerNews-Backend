/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.service;

import com.databasserne.hackernews.model.User;
import com.nimbusds.jose.JOSEException;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Vixo
 */
public class TokenServiceTest {
    
    private IToken tokenCtrl;

    public TokenServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.tokenCtrl = new TokenService();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testCreateTokenSuccess() throws JOSEException {
        User user = new User();
        user.setUsername("TestUser");
        String token = tokenCtrl.createToken(user);
        
        assertThat(token, notNullValue());
    }
}
