/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.databasserne.hackernews.repo;

import com.databasserne.hackernews.config.DatabaseCfg;
import com.databasserne.hackernews.repo.impl.SimulatorRepo;
import javax.persistence.Persistence;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kasper S. Worm
 */
public class SimulatorRepoTest {
    
    private static ISimulatorRepo simRepo;
    
    public SimulatorRepoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        simRepo = new SimulatorRepo(Persistence.createEntityManagerFactory(DatabaseCfg.PU_NAME_TEST));
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStatus method, of class SimulatorRepo.
     */
    @Test
    public void testGetStatus() {
        String res = simRepo.getStatus();
        String expRes = "ITS ALIVE!!!";
        
        assertThat(res, is(expRes));
    }

    /**
     * Test of getLatest method, of class SimulatorRepo.
     */
    @Test
    public void testGetLatest() {
        int res = simRepo.getLatest();
        int expRes = 4;
        
        assertThat(res, is(expRes));
        
    }
    
}
