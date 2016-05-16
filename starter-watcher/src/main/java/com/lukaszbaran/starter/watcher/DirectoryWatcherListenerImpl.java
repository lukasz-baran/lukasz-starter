package com.lukaszbaran.starter.watcher;

import org.apache.log4j.Logger;

import java.nio.file.Path;

public class DirectoryWatcherListenerImpl implements  DirectoryWatcherListener {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcherListenerImpl.class);

    @Override
    public void onEvent(Path path) {
        LOGGER.info("triggered on " + path.toAbsolutePath());
        // TODO implement
    }
}
