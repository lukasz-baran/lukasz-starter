package com.lukaszbaran.starter.processing;

import java.io.File;

public interface PictureProcessor {

    void handle(File file, String subject, String body) throws ProcessingException;
}
