package com.lukaszbaran.starter.watcher;

import java.nio.file.Path;

public interface DirectoryWatcherListener {

    void onEvent(Path path);

}
