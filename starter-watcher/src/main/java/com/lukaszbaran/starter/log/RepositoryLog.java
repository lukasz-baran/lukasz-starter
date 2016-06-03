package com.lukaszbaran.starter.log;


import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class remembers which files were already sent.
 * In future it will be archiving the information in real DB.
 */
public class RepositoryLog {

    private Set<String> storedFiles = Collections.synchronizedSet(new HashSet<String>());

    public void remember(File file) {
        storedFiles.add(file.getAbsolutePath());
    }

    public boolean isAlreadyStored(File file) {
        return storedFiles.contains(file.getAbsolutePath());
    }

}
