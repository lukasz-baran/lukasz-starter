package com.lukaszbaran.starter.watcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-app-context.xml")
public class SpringContextTest {

    @Autowired
    private DirectoryWatcher watcher;

    @Test
    public void smokeTest() {
        assertNotNull(watcher);
    }
}
