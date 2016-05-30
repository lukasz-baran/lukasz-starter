package com.lukaszbaran.starter.watcher;

import com.lukaszbaran.starter.processing.PictureProcessor;
import com.lukaszbaran.starter.processing.ProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Set;

public class DirectoryWatcherListenerImpl implements DirectoryWatcherListener {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherListenerImpl.class);

    private Set<CameraDescription> cameraDescriptions;
    private DirectoryWatcher directoryWatcher;
    private PictureProcessor pictureProcessor;

    /**
     * Path passed to this method should be absolute.
     * @param path
     */
    @Override
    public void onEvent(Path path) {
        if (path == null || StringUtils.isEmpty(path.toString())) {
            return;
        }
        String strPath = path.toString();
        LOGGER.info("triggered on " + strPath);
        if (strPath.endsWith("jpg")) {
            LOGGER.info("new image detected - send it");
            handlePicture(path);
        } else {
            // a new dir was created - add monitoring for that dir
            handleSubDir(path);
        }
    }

    private void handlePicture(Path path) {
        File file = new File(path.toString());
        final long fileSize = file.length();
        LOGGER.debug("file " + path.toString() + " size = " + fileSize);
        if (fileSize < 1) {
            return;
        }

        if (pictureProcessor != null) {
            try {
                pictureProcessor.handle(file, constructSubject(path.toString()), constructBody());
                // TODO if successful should add information to MySQL log

            } catch (ProcessingException e) {
                // TODO think about adding the message to Non-Processed-Queue
                LOGGER.error("EMAIL not sent!");
            }
        }
    }

    private String constructSubject(String path) {
        for (CameraDescription cd : cameraDescriptions) {
            if (path.contains(cd.getDirectory())) {
                return cd.getName();
            }
        }
        return "unknown camera";
    }


    private String constructBody() {
        // TODO improve this method
        return "Picture taken at " + new Date();
    }


    private void handleSubDir(Path path) {
        Path subdirWithPics = Paths.get(path.toString(), "01", "pic");
        // wait for the sub dirs
        LOGGER.debug("waiting for " + subdirWithPics);
        try {
            int counter = 0;

            while (!subdirWithPics.toFile().exists()) {
                Thread.sleep(100);
                counter++;
                LOGGER.debug("waiting for " + subdirWithPics + "...");

                if (counter == 100) {
                    // safety condition - if the sub directories were not created after 10 seconds, we're giving up
                    LOGGER.warn("seems that " + subdirWithPics +
                            " was not created after 10 seconds - skipping");
                    return;
                }
            }

        } catch (InterruptedException ie) {
            LOGGER.warn("thread got interrupted, exiting listener");
            return;
        }

        LOGGER.info("subdirectory with pictures: " + subdirWithPics);
        CameraDescription description = new CameraDescription("picture_folder", subdirWithPics.toString());
        directoryWatcher.registerDirectoryIfPossible(description);

    }


    public void setCameraDescriptions(Set<CameraDescription> cameraDescriptions) {
        this.cameraDescriptions = cameraDescriptions;
    }

    public void setDirectoryWatcher(DirectoryWatcher watcher) {
        this.directoryWatcher = watcher;
    }

    public void setPictureProcessor(PictureProcessor pictureProcessor) {
        this.pictureProcessor = pictureProcessor;
    }


}
