package ru.sibint.olymp.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EMailSender {
    public void SendEmail(String email, String contestToken) {
        String to = email;
        String from = "Tyumen_ACM_Society";
        String host = "smtp.gmail.com";
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", host);
        props.put("mail.stmp.user" , "acm.utmn@gmail.com");
        props.put("mail.smtp.password", "475508th");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        Session session = Session.getDefaultInstance(props, new SmtpAuthenticator());

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Registration on acm.utmn.ru");
            message.setText("Hello! Thank you for registration on acm.utmn.ru! Use next token to submit tasks: " + contestToken);
            Transport t = session.getTransport("smtp");
            t.send(message);
            System.out.println("Message was successfully sent.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    class SmtpAuthenticator extends Authenticator {
        public SmtpAuthenticator() {

            super();
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "acm.utmn";
            String password = "475508th";
            return new PasswordAuthentication(username, password);
        }
    }

}
