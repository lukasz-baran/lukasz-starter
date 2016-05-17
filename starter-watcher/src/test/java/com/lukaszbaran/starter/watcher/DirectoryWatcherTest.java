package com.lukaszbaran.starter.watcher;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DirectoryWatcherTest {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherTest.class);
    private static final String TEST_DIR = "testFolder";
    private static final String DIR_TO_CREATE = "2015-12-12";

    private final DirectoryWatcher watcher = new DirectoryWatcher();

    @BeforeClass
    public static void onlyOnce() {
        final File testDir = Paths.get(TEST_DIR).toFile();
        if (testDir.exists()) {
            LOGGER.warn("path " + TEST_DIR + " exists. deleting");
            FileSystemUtils.deleteRecursively(testDir);
        }
    }

    @Before
    public void setUp() throws IOException {
        Path currentDir = Paths.get(".");
        String strCurrDir = currentDir.toAbsolutePath().toString() + File.separatorChar + TEST_DIR;

        Files.createDirectory(Paths.get(TEST_DIR));
        LOGGER.debug(strCurrDir);

        CameraDescription description = mock(CameraDescription.class);
        when(description.getDirectory()).thenReturn(strCurrDir);

        watcher.setDir2watch(singletonList(description));
    }

    @Test
    public void shouldReactToDirChanges() throws Exception {
        watcher.afterPropertiesSet();
        Thread thread = new Thread(watcher);
        watcher.setListener((Path path) -> {
                    LOGGER.info("path " + path);
                    watcher.setRunning(false);
                    assertTrue(path.endsWith(DIR_TO_CREATE));
                });
        thread.start();
        Thread.sleep(1000);
        assertTrue(Paths.get(TEST_DIR + File.separatorChar + DIR_TO_CREATE).toFile().mkdir());
    }

}
