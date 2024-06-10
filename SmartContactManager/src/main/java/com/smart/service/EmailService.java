package com.smart.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String message, String subject, String to) {
        boolean flag=false;
        String from = "haseeb.odho03@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        final String userName="haseeb.odho03@gmail.com";
        final String password="tnfi onbf thei ttrf";
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(userName, password);

            }
        });
        session.setDebug(true);

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            flag=true;
            System.out.println("Email Sent");
        } catch (MessagingException e) {
            System.out.println(e);
        }
        return  flag;

    }
}
