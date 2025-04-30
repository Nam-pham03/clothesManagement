//package com.example.project_prm.Utils;
//
//
//
//
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
//import java.util.Properties;
//
//import jakarta.mail.Message;
//import jakarta.mail.MessagingException;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//
//
//public class EmailSender {
//    private final String email;
//    private final String password;
//
//
//
//    public EmailSender(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }
//
//    public boolean sendEmail(String toEmail, String subject, String body) {
//        try {
//            Properties props = new Properties();
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", "smtp.gmail.com");
//            props.put("mail.smtp.port", "587");
//            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//
//            Session session = Session.getInstance(props, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(email, password);
//                }
//            });
//
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(email));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            message.setSubject(subject);
//            message.setText(body);
//
//            Transport.send(message);
//            return true; // Gửi thành công
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return false; // Gửi thất bại
//        }
//    }
//}
