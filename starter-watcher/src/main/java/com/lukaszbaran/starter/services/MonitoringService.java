package com.lukaszbaran.starter.services;

import com.lukaszbaran.starter.watcher.CameraDescription;

import java.util.Set;

public interface MonitoringService {
    Set<CameraDescription> getAll();
    CameraDescription getById(int id);

    boolean getStatus();
    void enable(boolean enable);
}
