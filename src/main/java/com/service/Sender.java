package com.service;

import lombok.Builder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Builder
public class Sender {
    private String username;
    private String password;
    private Properties props;

//    public Sender(String username, String password) {
//        this.username = username;
//        this.password = password;
//
//        props = new Properties();
//        props.setProperty("mail.smtp.host", "smtp.yandex.ru");
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//        props.setProperty("mail.smtp.socketFactory.port", "465");
//        props.setProperty("mail.smtp.auth", "true");
//    }

    public void send(String subject, String text, String toEmail){
        Session session = Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //from whom
            message.setFrom(new InternetAddress(username));
            //to whom
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //email subject
            message.setSubject(subject);
            //email body
            message.setText(text);

            //send email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}