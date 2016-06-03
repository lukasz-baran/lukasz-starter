package com.lukaszbaran.starter.watcher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class DirectoryWatcher implements InitializingBean, Runnable, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class);
    private WatchService watcher;
    private Set<CameraDescription> dir2watch;

    private DirectoryWatcherListener listener;
    private ExecutorService executorService;

    private volatile boolean isRunning;

    public DirectoryWatcher() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void afterPropertiesSet() throws Exception {
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            LOGGER.error("unable to create WatchService", e);
            return;
        }
        for (CameraDescription desc : dir2watch) {
            registerDirectory(desc);
        }
        executorService.execute(this);
    }

    private void registerDirectory(CameraDescription desc) {
        LOGGER.info("registering directory " + desc.getDirectory() + " for camera '" + desc.getName() + "'");
        Path dir = Paths.get(desc.getDirectory());
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
        } catch (IOException x) {
            LOGGER.error("unable to register event", x);
        }
        LOGGER.info("directory " + desc.getDirectory() + " is now being watched");
    }

    void registerDirectoryIfPossible(CameraDescription desc) {
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
            if (watchKey == null) {
                continue;
            }
            for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                Path path = (Path) watchEvent.context();
                Path rootPath = (Path) watchKey.watchable();
                Path pathToRegister = Paths.get(rootPath.toString(), path.toString());
                LOGGER.debug("listener " + watchEvent.kind() + " " + pathToRegister);

                // for files we react only on ENTRY_MODIFY
//                if ("ENTRY_CREATE".equalsIgnoreCase(watchEvent.kind().toString()) &&
//                        !pathToRegister.toFile().isDirectory()) {
//                    continue;
//                }

                // for directories we react only on ENTRY_CREATE
//                if ("ENTRY_MODIFY".equalsIgnoreCase(watchEvent.kind().toString()) &&
//                        pathToRegister.toFile().isDirectory()) {
//                    continue;
//                }

                if (listener != null) {
                    listener.onEvent(pathToRegister);
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

    @Override
    public void destroy() throws Exception {
        LOGGER.warn("Stopping DirectoryWatcher");
        isRunning = false;
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        watcher.close();
    }
}
