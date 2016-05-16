package com.lukaszbaran.starter.watcher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DirectoryWatcher implements InitializingBean, Runnable {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class);
    private final WatchService watcher;
    private String dir2watch;

    private DirectoryWatcherListener listener;

    private volatile boolean isRunning;

    public DirectoryWatcher() {
        WatchService _watcher = null;
        try {
            _watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            LOGGER.error("unable to create WatchService", e);
        }
        watcher = _watcher;
    }


    public void afterPropertiesSet() throws Exception {
        LOGGER.info("dir watcher will start on dir: " + dir2watch);
        if (watcher == null) {
            LOGGER.error("cannot start watching (null)");
            return;
        }
        Path dir = Paths.get(dir2watch);
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE,ENTRY_MODIFY);
        } catch (IOException x) {
            LOGGER.error("unable to register event", x);
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (true) {
            if (!isRunning) {
                LOGGER.info("stopping watcher thread");
                break;
            }

            WatchKey  watchKey = watcher.poll();
// another way
//            try {
//                watchKey = watcher.take();
//            } catch (InterruptedException ex) {
//                System.out.println("InterruptedException: " + ex);
//                break;
//            }
            if (watchKey == null) {
                continue;
            }
            for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                Path path = (Path) watchEvent.context();
                LOGGER.debug("listener " + watchEvent.kind() + " " + path);

                if (listener != null) {
                    listener.onEvent(path);
                }
            }
            watchKey.reset();
        }
    }

    public void setDir2watch(String dir2watch) {
        this.dir2watch = dir2watch;
    }

    public void setListener(DirectoryWatcherListener listener) {
        this.listener = listener;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
