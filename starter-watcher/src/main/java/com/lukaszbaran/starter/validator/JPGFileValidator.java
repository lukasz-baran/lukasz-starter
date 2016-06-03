package com.lukaszbaran.starter.validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JPGFileValidator {

    private static final byte HEADER_MAGIC_BYTE_1 = (byte)0xff;
    private static final byte HEADER_MAGIC_BYTE_2 = (byte)0xd8;

    private static final byte FOOTER_MAGIC_BYTE_1 = (byte)0xff;
    private static final byte FOOTER_MAGIC_BYTE_2 = (byte)0xd9;

    public boolean isValid(File file) {
        try {
            byte[] content = Files.readAllBytes(file.toPath());
            if (content == null || content.length < 100) {
                return false;
            }
            if (content[0] != HEADER_MAGIC_BYTE_1 || content[1] != HEADER_MAGIC_BYTE_2) {
                return false;
            }
            int length = content.length;
            if (content[length - 2] != FOOTER_MAGIC_BYTE_1 || content[length - 1] != FOOTER_MAGIC_BYTE_2) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
