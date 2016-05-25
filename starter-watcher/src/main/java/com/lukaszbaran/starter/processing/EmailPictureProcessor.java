package com.lukaszbaran.starter.processing;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

public class EmailPictureProcessor implements PictureProcessor {
    @Override
    public void handle(File file) throws ProcessingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("username@gmail.com","password");
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("frommail@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("tomail@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Test Mail");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new ProcessingException("Unable to send email message",  e);
        }
    }
}
