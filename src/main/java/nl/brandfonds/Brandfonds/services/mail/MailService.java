package nl.brandfonds.Brandfonds.services.mail;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    @Value("${urls.resetPasswordUrl}")
    private String forgotPasswordBaseUrl;

    @Value("${urls.registrationUrl}")
    private String registrationBaseUrl;

    @Value("${urls.userActivationUrl}")
    private String activateUserUrl;

    @Value("${spring.mail.username}")
    private String sendEmailAddress;


    //BE AWARE AVG MUST BE OFF BEFORE TESTING

    public void sendRegisterMail(String recipient, String registrationCode) {
        var content = readHtmlFileAsString("/html_pages/Register.html");
        content = content.replace("[REGISTRATION LINK]", registrationBaseUrl + registrationCode);
        sendMail(recipient, "Registratie brandfonds", content);
        log.info("A registration conformation mail was send to {}", recipient);
    }

    public void sendChangePasswordMail(String recipient, String forgotPasswordCode) {
        var content = readHtmlFileAsString("/html_pages/ForgotPassword.html");
        content = content.replace("[RESET PASSWORD LINK]", forgotPasswordBaseUrl + forgotPasswordCode);
        sendMail(recipient, "Wachtwoord reset brandfonds", content);
        log.info("A password reset mail was send to {}", recipient);
    }

    public void sendUserActivationMail(String recipient, String email, String username, Integer id) {
        var content = readHtmlFileAsString("/html_pages/ActivateUser.html");
        content = content.replace("[ACTIVATE USER URL]", activateUserUrl + id).replace("[USERNAME]", username).replace("[EMAIL]", email);
        sendMail(recipient, "Gebruiker activeren brandfonds", content);
        log.info("A activation request mail was send to {}", recipient);
    }

    public void sendUserActivatedMail(String recipient, String email, String username) {
        var content = readHtmlFileAsString("/html_pages/ActivatedUser.html");
        content = content.replace("[USERNAME]", username).replace("[EMAIL]", email);
        sendMail(recipient, "Gebruiker geactiveerd brandfonds", content);
        log.info("A activition conformation mail was send to {}", recipient);
    }

    private String readHtmlFileAsString(String path) {
        var resource = new ClassPathResource(path);
        try {
            byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(dataArr, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMail(String recipient, String subject, String messageContent) {
        var t = new Thread(() -> {
            try {
                mailConfig.sendEmail(recipient, sendEmailAddress, subject, messageContent);
            } catch (MessagingException messagingException) {
                throw new RuntimeException(messagingException);
            }
        });
        t.start();
    }

}
