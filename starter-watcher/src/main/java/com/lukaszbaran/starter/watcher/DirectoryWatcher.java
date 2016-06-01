package com.lukaszbaran.starter.watcher;

import com.lukaszbaran.starter.utils.CommandExecutor;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class DirectoryWatcher implements InitializingBean, Runnable, DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(DirectoryWatcher.class);
    private final WatchService watcher;
    private Set<CameraDescription> dir2watch;

    private DirectoryWatcherListener listener;
    private ThreadPoolTaskExecutor taskExecutor;

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
        for (CameraDescription desc : dir2watch) {
            registerDirectory(desc);
        }

        taskExecutor.submit(this);
    }

    private void registerDirectory(CameraDescription desc) {
        if (SystemUtils.IS_OS_UNIX) {
            LOGGER.info("lets do some checkup");
            CommandExecutor.run("hostname");
            CommandExecutor.run("pwd");
            CommandExecutor.run("ls -al /");
            CommandExecutor.run("ls -al");
            CommandExecutor.run("ls -al /etc");
            CommandExecutor.run("ls -al /home/barranek/kamera");
            CommandExecutor.run("env");
            CommandExecutor.run("ls -al /usr/local/tomcat/vhosts/barranek.linuxpl.eu/kamera/");
            CommandExecutor.run("whoami");
        }

        LOGGER.info("registering directory " + desc.getDirectory() + " for camera '" + desc.getName() + "'");
        Path dir = Paths.get(desc.getDirectory());
        try {
            WatchKey key = dir.register(watcher, ENTRY_CREATE);
        } catch (IOException x) {
            LOGGER.error("unable to register event", x);
        }
        LOGGER.info("directory " + desc.getDirectory() + " is now being watched");
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

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.warn("Stopping DirectoryWatcher");
        setRunning(false);
    }
}
