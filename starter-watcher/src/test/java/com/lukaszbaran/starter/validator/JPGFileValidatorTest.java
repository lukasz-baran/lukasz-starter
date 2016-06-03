package com.lukaszbaran.starter.validator;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JPGFileValidatorTest {

    private final JPGFileValidator validator = new JPGFileValidator();

    @Test
    public void shouldReturnTrueForCorrectJPG() {
        File f = new File(Resources.getResource("bin/1518500082.jpg").getFile());
        assertTrue(validator.isValid(f));
    }

    @Test
    public void shouldReturnFalseForInvalidJPG() {
        File f = new File(Resources.getResource("bin/invalid_jpg.jpg").getFile());
        assertFalse(validator.isValid(f));
    }

}
