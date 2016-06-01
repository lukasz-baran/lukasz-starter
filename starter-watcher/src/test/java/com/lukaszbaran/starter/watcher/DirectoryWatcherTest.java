package com.lukaszbaran.starter.watcher;

import com.google.common.io.Resources;
import com.lukaszbaran.starter.processing.PictureProcessor;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DirectoryWatcherTest {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherTest.class);
    private static final String TEST_DIR = "testFolder";
    private static final String DIR_TO_CREATE = "2015-12-12";
    private static final String FAKE_CAMERA_NAME = "kamera testowa";

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
        Path created = Files.createDirectory(Paths.get(TEST_DIR));
        LOGGER.debug(created.toAbsolutePath());

        CameraDescription description = mock(CameraDescription.class);
        when(description.getDirectory()).thenReturn(created.toAbsolutePath().toString());

        Set<CameraDescription> set = new HashSet<>();
        set.add(description);
        watcher.setDir2watch(set);
    }

    @After
    public void cleanUp() {
        onlyOnce();
    }

    @Test
    public void shouldReactToDirChanges() throws Exception {
        watcher.afterPropertiesSet();
        Thread thread = new Thread(watcher);
        watcher.setListener(new DirectoryWatcherListener() {
            @Override
            public void onEvent(Path path) {
                LOGGER.info("path " + path);
                watcher.setRunning(false);
                assertTrue(path.endsWith(DIR_TO_CREATE));

                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    fail("unable to delete " + path);
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        File directory = Paths.get(TEST_DIR + File.separatorChar + DIR_TO_CREATE).toFile();
        assertTrue(directory.mkdir());
    }

    @Test
    public void shouldReactToDirChangeWithRealListener() throws Exception {
        watcher.afterPropertiesSet();
        Thread thread = new Thread(watcher);
        DirectoryWatcherListenerImpl listener = new DirectoryWatcherListenerImpl();
        listener.setDirectoryWatcher(watcher);

        Set<CameraDescription> descriptions = new HashSet<>();
        CameraDescription description = new CameraDescription(FAKE_CAMERA_NAME, Paths.get(TEST_DIR).toAbsolutePath().toString());
        descriptions.add(description);
        listener.setCameraDescriptions(descriptions);

        watcher.setListener(listener);

        PictureProcessor processorMock = mock(PictureProcessor.class);
        listener.setPictureProcessor(processorMock);

        thread.start();
        Thread.sleep(1000);
        File dirWithDate = Paths.get(TEST_DIR + File.separatorChar + DIR_TO_CREATE).toFile();
        assertTrue(dirWithDate.mkdir());

        File dirWith01 = Paths.get(dirWithDate.getAbsolutePath(), "01").toFile();
        assertTrue(dirWith01.mkdir());

        File dirWithPic = Paths.get(dirWith01.getAbsolutePath(), "pic").toFile();
        assertTrue(dirWithPic.mkdir());

        Path toCreate = Paths.get(dirWithPic.getAbsolutePath(), "1518500082.jpg");
        URL fileJPG = Resources.getResource("bin/1518500082.jpg");
        Long size = Files.copy(fileJPG.openStream(), toCreate);
        assertThat(size, greaterThan(0L));

        Thread.sleep(1000);
        watcher.setRunning(false);
        thread.join();
        verify(processorMock).handle(eq(toCreate.toFile()), eq(FAKE_CAMERA_NAME), any(String.class));
    }

}
