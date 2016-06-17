package com.lukaszbaran.starter.services;

import com.lukaszbaran.starter.watcher.CameraDescription;
import com.lukaszbaran.starter.watcher.DirectoryWatcher;
import org.apache.log4j.Logger;

import java.util.Set;

public class MonitoringServiceImpl implements MonitoringService {
    private static final Logger LOGGER = Logger.getLogger(MonitoringServiceImpl.class);

    private Set<CameraDescription> cameraDescriptions;
    private DirectoryWatcher directoryWatcher;

    @Override
    public Set<CameraDescription> getAll() {
        return cameraDescriptions;
    }

    @Override
    public CameraDescription getById(int id) {
        // TODO
        return null;
    }

    @Override
    public boolean getStatus() {
        return directoryWatcher.getIsRunning();
    }

    @Override
    public void enable(boolean enable) {
        LOGGER.info("enable() = " + enable);
        if (enable) {
            try {
                directoryWatcher.afterPropertiesSet();
            } catch (Exception e) {
                LOGGER.error("Unable to start DirectoryWatcher!", e);
            }
        } else {
            try {
                directoryWatcher.destroy();
            } catch (Exception e) {
                LOGGER.error("Error while destroying DirectoryWatcher!", e);
            }
        }
    }

    public void setDirectoryWatcher(DirectoryWatcher directoryWatcher) {
        this.directoryWatcher = directoryWatcher;
    }

    public void setCameraDescriptions(Set<CameraDescription> cameraDescriptions) {
        this.cameraDescriptions = cameraDescriptions;
    }

}
