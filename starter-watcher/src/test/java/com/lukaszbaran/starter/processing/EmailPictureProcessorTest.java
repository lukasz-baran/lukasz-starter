package com.lukaszbaran.starter.processing;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class EmailPictureProcessorTest {

    private static final String TEST_EMAIL_SUBJECT = "testing subject";
    private static final String TEST_EMAIL_BODY = "testing body";

    private final EmailPictureProcessor processor = new EmailPictureProcessor();

    @Test
    public void shouldSendSampleEmail() throws Exception {
        SimpleSmtpServer server = SimpleSmtpServer.start();

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "localhost");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "25");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.login", "kamera@barranek.linuxpl.eu");
        properties.setProperty("mail.password", "xxx");
        properties.setProperty("mail.sender", "lukasz.baran@gmail.com");
        properties.setProperty("mail.recipients", "lukasz.baran@gmail.com");

        processor.setProps(properties);

        File f = new File(Resources.getResource("bin/1518500082.jpg").getFile());
        processor.handle(f, TEST_EMAIL_SUBJECT, TEST_EMAIL_BODY);

        server.stop();

        assertTrue(server.getReceivedEmailSize() == 1);
        Iterator emailIter = server.getReceivedEmail();
        SmtpMessage email = (SmtpMessage) emailIter.next();
        assertTrue(email.getHeaderValue("Subject").equals(TEST_EMAIL_SUBJECT));
        assertTrue(email.getBody().contains(TEST_EMAIL_BODY));
    }
}
