package com.lukaszbaran.starter.processing;

import java.io.File;

public interface PictureProcessor {

    void handle(File file) throws ProcessingException;
}
