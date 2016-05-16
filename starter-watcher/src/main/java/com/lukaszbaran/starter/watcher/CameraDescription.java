package com.lukaszbaran.starter.watcher;

public class CameraDescription {

    private final String name;
    private final String directory;

    public CameraDescription(String name, String directory) {
        this.name = name;
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }


}
