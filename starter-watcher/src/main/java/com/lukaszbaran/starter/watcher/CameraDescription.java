package com.lukaszbaran.starter.watcher;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CameraDescription {

    private String name;
    private String directory;

    public CameraDescription() {}

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
