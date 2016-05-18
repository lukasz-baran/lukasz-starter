package com.lukaszbaran.starter.watcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class DirectoryWatcherListenerImpl implements DirectoryWatcherListener {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherListenerImpl.class);

    private Set<CameraDescription> cameraDescriptions;
    private DirectoryWatcher directoryWatcher;

    @Override
    public void onEvent(Path path) {
        if (path == null || StringUtils.isEmpty(path.toAbsolutePath().toString())) {
            return;
        }

        String strPath = path.toAbsolutePath().toString();
        LOGGER.info("triggered on " + path.toAbsolutePath());

        if (strPath.endsWith("jpg")) {
            // handle image TODO

            LOGGER.info("new image detected - send it");
        } else {
            // a new folder was created - add monitoring for that folder
            String subdirWithPics = Paths.get(path.toAbsolutePath().toString(), "01", "pic").toAbsolutePath().toString();
            LOGGER.info("subdirectory with pictures: " + subdirWithPics);
            CameraDescription description = new CameraDescription("picture_folder", subdirWithPics);
            directoryWatcher.registerDirectoryIfPossible(description);
        }
    }

    public void setCameraDescriptions(Set<CameraDescription> cameraDescriptions) {
        this.cameraDescriptions = cameraDescriptions;
    }

    public void setDirectoryWatcher(DirectoryWatcher watcher) {
        this.directoryWatcher = watcher;
    }

}
