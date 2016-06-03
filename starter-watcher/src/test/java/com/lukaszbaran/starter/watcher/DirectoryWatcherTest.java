package com.lukaszbaran.starter.watcher;

import com.google.common.io.Resources;
import com.lukaszbaran.starter.log.RepositoryLog;
import com.lukaszbaran.starter.processing.PictureProcessor;
import com.lukaszbaran.starter.validator.JPGFileValidator;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DirectoryWatcherTest {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherTest.class);
    private static final String DIR_TO_CREATE = "2015-12-12";
    private static final String FAKE_CAMERA_NAME = "kamera testowa";

    private Path pathTmpDir;
    private DirectoryWatcher watcher;

    private static void cleanDir(Path tempDir) {
        final File testDir = tempDir.toFile();
        if (testDir.exists()) {
            LOGGER.warn("path " + tempDir + " exists. deleting");
            FileSystemUtils.deleteRecursively(testDir);
        }
    }

    @Before
    public void setUp() throws IOException {
        watcher = new DirectoryWatcher();

        pathTmpDir = Files.createTempDirectory("watcher");
        pathTmpDir.toFile().deleteOnExit();
        LOGGER.debug(pathTmpDir.toAbsolutePath());
        CameraDescription description = mock(CameraDescription.class);
        when(description.getDirectory()).thenReturn(pathTmpDir.toAbsolutePath().toString());
        Set<CameraDescription> set = new HashSet<>();
        set.add(description);
        watcher.setDir2watch(set);
    }

    @After
    public void cleanUp() {
        cleanDir(pathTmpDir);
    }

    @Test
    //@Ignore("not needed anymore")
    public void shouldReactToDirChanges() throws Exception {
        watcher.afterPropertiesSet();
        watcher.setListener(new DirectoryWatcherListener() {
            @Override
            public void onEvent(Path path) {
                LOGGER.info("path " + path);
                assertTrue(path.endsWith(DIR_TO_CREATE));

                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    fail("unable to delete " + path);
                }
            }
        });
        Thread.sleep(1000);
        File directory = Paths.get(pathTmpDir.toString(), DIR_TO_CREATE).toFile();
        assertTrue(directory.mkdir());
    }

    @Test
    public void shouldReactToDirChangeWithRealListener() throws Exception {
        DirectoryWatcherListenerImpl listener = new DirectoryWatcherListenerImpl();
        listener.setDirectoryWatcher(watcher);

        Set<CameraDescription> descriptions = new HashSet<>();
        CameraDescription description = new CameraDescription(FAKE_CAMERA_NAME, pathTmpDir.toAbsolutePath().toString());
        descriptions.add(description);
        listener.setCameraDescriptions(descriptions);

        RepositoryLog repoLog = mock(RepositoryLog.class);
        listener.setRepositoryLog(repoLog);

        JPGFileValidator fileValidatorMock = mock(JPGFileValidator.class);
        when(fileValidatorMock.isValid(any(File.class))).thenReturn(Boolean.TRUE);
        listener.setFileValidator(fileValidatorMock);

        watcher.setListener(listener);

        PictureProcessor processorMock = mock(PictureProcessor.class);
        listener.setPictureProcessor(processorMock);

        watcher.afterPropertiesSet();

        File dirWithDate = Paths.get(pathTmpDir.toString(), DIR_TO_CREATE).toFile();
        assertTrue(dirWithDate.mkdir());

        File dirWith01 = Paths.get(dirWithDate.getAbsolutePath(), "01").toFile();
        assertTrue(dirWith01.mkdir());

        File dirWithPic = Paths.get(dirWith01.getAbsolutePath(), "pic").toFile();
        assertTrue(dirWithPic.mkdir());

        Thread.sleep(3000);

        File expectedFile = copyFile(dirWithPic.getAbsolutePath());

        verify(processorMock, timeout(10000)).handle(eq(expectedFile), eq(FAKE_CAMERA_NAME), any(String.class));
        verify(repoLog).isAlreadyStored(eq(expectedFile));
        verify(repoLog).remember(eq(expectedFile));
        watcher.destroy();
    }


    private File copyFile(String where) throws IOException {
        Path toCreate = Paths.get(where, "1518500082.jpg");
        URL fileJPG = Resources.getResource("bin/1518500082.jpg");
        try (InputStream is = fileJPG.openStream()) {
            Long size = Files.copy(is, toCreate);
            assertThat(size, greaterThan(0L));
        }
        return toCreate.toFile();
    }

}
