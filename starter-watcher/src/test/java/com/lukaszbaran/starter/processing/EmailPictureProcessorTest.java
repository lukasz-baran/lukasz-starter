package com.lukaszbaran.starter.processing;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;

public class EmailPictureProcessorTest {


    @Test
    public void shouldSendSampleEmail() throws Exception {
        EmailPictureProcessor processor = new EmailPictureProcessor();
        File f = new File(Resources.getResource("bin/1518500082.jpg").getFile());
        processor.handle(f);
    }
}
