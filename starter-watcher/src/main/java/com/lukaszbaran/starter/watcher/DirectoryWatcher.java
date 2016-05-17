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
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DirectoryWatcher implements InitializingBean, Runnable {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class);
    private final WatchService watcher;
    private Set<CameraDescription> dir2watch;

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
        if (watcher == null) {
            LOGGER.error("cannot start watching (null)");
            return;
        }
        dir2watch.forEach(desc -> registerDirectory(desc));
    }

    private void registerDirectory(CameraDescription desc) {
        LOGGER.info("registering directory " + desc.getDirectory() + " for camera '" + desc.getName() + "'");
        Path dir = Paths.get(desc.getDirectory());
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException x) {
            LOGGER.error("unable to register event", x);
        }
    }

    public void registerDirectoryIfPossible(CameraDescription desc) {
        if (dir2watch.contains(desc)) {
            LOGGER.warn("directory " + desc.getDirectory() + " already registered");
            return;
        }
        registerDirectory(desc);
        dir2watch.add(desc);
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

    public void setDir2watch(Set<CameraDescription> dir2watch) {
        this.dir2watch = dir2watch;
    }

    public void setListener(DirectoryWatcherListener listener) {
        this.listener = listener;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
