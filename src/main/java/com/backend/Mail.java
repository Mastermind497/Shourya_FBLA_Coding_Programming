package com.backend;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mail {
    public static void sendMessage(String subject, String body) {
        MySQLMethods.setUp();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        try {
            String            to        = "shouryabansal2012@gmail.com, " + MySQLMethods.getEmails();
            InternetAddress[] addresses = InternetAddress.parse(to, true);
            
            for (InternetAddress recipient : addresses) {
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("fbla.genie@gmail.com", "geniepass");
                    }
                });
                MimeMessage message = new MimeMessage(session);
                message.setRecipient(Message.RecipientType.TO, /*new InternetAddress[] {recipient}*/ recipient);
                message.setSubject("FBLA Announcement: " + subject);
                message.setSentDate(new Date());
                message.setText(body);
                message.setHeader("XPriority", "1");
                
                try {
                    Notification success = new Notification();
                    success.setText("Successfully sent to " + recipient.toString());
                    success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    success.setDuration(1000);
                    success.open();
                } catch (Exception a) {
                    System.out.println("Successfully sent to " + recipient);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        MySQLMethods.setUp();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        try {
            String            to        = "shouryabansal2012@gmail.com, " + MySQLMethods.getEmails();
            InternetAddress[] addresses = InternetAddress.parse(to, true);

//            for (InternetAddress recipient : addresses) {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("fbla.genie@gmail.com", "geniepass");
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, /*new InternetAddress[] {recipient}*/ addresses);
            message.setSubject("sdfadest");
            message.setSentDate(new Date());
            message.setText("Hello, I amasfasdfafdsafdsdf John. This is a test for whether or not you are actually getting this email");
            message.setHeader("XPriority", "1");

//                try {
//                    Notification success = new Notification();
//                    success.setText("Successfully sent to " + recipient.toString());
//                    success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//                    success.setDuration(1000);
//                    success.open();
//                }
//                catch (Exception a) {
//                    System.out.println("Successfully sent to " + recipient);
//                }
//            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
