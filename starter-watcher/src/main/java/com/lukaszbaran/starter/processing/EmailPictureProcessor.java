package com.lukaszbaran.starter.processing;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailPictureProcessor implements PictureProcessor, InitializingBean {
    private static final Logger LOGGER = Logger.getLogger(EmailPictureProcessor.class);

    private Properties props;

    @Override
    public void handle(File file, String subject, String body) throws ProcessingException {
        if (props == null || props.isEmpty()) {
            LOGGER.error("EmailPictureProcessor cannot work as its properties are not configured");
            return;
        }

        final String loginEmail = props.getProperty("mail.login");
        final String passwordEmail = props.getProperty("mail.password");
        final String emailSender = props.getProperty("mail.sender");
        final String emailRecipients = props.getProperty("mail.recipients");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(loginEmail, passwordEmail);
                    }
                });
        try {
            MimeMessage mimeMessage = buildMimeMessage(file, subject, body, session, new InternetAddress(emailSender),
                 InternetAddress.parse(emailRecipients)
            );
            Transport.send(mimeMessage);
            LOGGER.debug("Done - email sent");
        } catch (MessagingException | IOException e) {
            throw new ProcessingException("Unable to send email message",  e);
        }
    }


    private MimeMessage buildMimeMessage(File file, String subject, String body, Session session, InternetAddress from, InternetAddress[] to) throws MessagingException, IOException {

        // create a message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(from);
        //InternetAddress[] address = {new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, to);
        msg.setSubject(subject);

        // create and fill the first message part
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(body);

        // create the second message part
        MimeBodyPart mbp2 = new MimeBodyPart();

        // attach the file to the message
        mbp2.attachFile(file);

	    /*
	     * Use the following approach instead of the above line if
	     * you want to control the MIME type of the attached file.
	     * Normally you should never need to do this.
	     *
	    FileDataSource fds = new FileDataSource(filename) {
		public String getContentType() {
		    return "application/octet-stream";
		}
	    };
	    mbp2.setDataHandler(new DataHandler(fds));
	    mbp2.setFileName(fds.getName());
	     */

        // create the Multipart and add its parts to it
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);

        // add the Multipart to the message
        msg.setContent(mp);

        // set the Date: header
        msg.setSentDate(new Date());

	    /*
	     * If you want to control the Content-Transfer-Encoding
	     * of the attached file, do the following.  Normally you
	     * should never need to do this.
	     *
	    msg.saveChanges();
	    mbp2.setHeader("Content-Transfer-Encoding", "base64");
	     */
        return msg;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("EmailPictureProcessor initialized");
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
