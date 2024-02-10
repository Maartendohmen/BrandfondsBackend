package nl.brandfonds.Brandfonds.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Date;

@Configuration
public class MailConfig {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String recipient, String from, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setFrom("noreply@brandfonds.nl");
        helper.setSubject(subject);
        helper.setText(body, true); // Set the second parameter to true for HTML content
        helper.setSentDate(new Date());

        javaMailSender.send(message);
    }

}
