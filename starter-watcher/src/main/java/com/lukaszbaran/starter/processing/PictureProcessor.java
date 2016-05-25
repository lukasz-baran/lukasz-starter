package com.lukaszbaran.starter.processing;

import javax.mail.internet.AddressException;
import java.io.File;

public interface PictureProcessor {

    void handle(File file) throws ProcessingException;
}
