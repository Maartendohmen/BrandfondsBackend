package nl.brandfonds.Brandfonds.implementation;

import nl.brandfonds.Brandfonds.BrandfondsApplication;
import nl.brandfonds.Brandfonds.abstraction.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class MailImpl implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailImpl.class);

    @Value("${automaticmail.mailadres}")
    private String mailAdres;

    @Value("${automaticmail.password}")
    private String mailPassword;

    @Value("${resetpassword.url}")
    private String forgotPasswordBaseUrl;

    @Value("${registration.url}")
    private String registrationBaseUrl;

    @Value("${activateuser.url}")
    private String activateUserUrl;

    private Properties mailproperties;
    private Session mailSession;

    //BE AWARE AVG MUST BE OFF BEFORE TESTING

    public void sendRegisterMail(String receipent, String registrationCode) {
        readInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailAdres));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Registratie brandfonds");

                //Readout html page
                String data = "";
                ClassPathResource resource = new ClassPathResource("/html_pages/Register.html");
                try {
                    byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
                    data = new String(dataArr, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    // do whatever
                }

                String newhtmlpage = data.replace("registratielink", registrationBaseUrl + registrationCode);

                message.setContent(newhtmlpage, "text/html");

                Transport.send(message);

                logger.info("A registration conformation mail was send to {}", receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();

    }

    public void sendChangePasswordMail(String receipent, String forgotPasswordCode) {
        readInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailAdres));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Wachtwoord reset brandfonds");

                //Readout html page
                String data = "";
                ClassPathResource resource = new ClassPathResource("/html_pages/ForgotPassword.html");
                try {
                    byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
                    data = new String(dataArr, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    // do whatever
                }

                String newhtmlpage = data.replace("resetpasswordlink", forgotPasswordBaseUrl + forgotPasswordCode);

                message.setContent(newhtmlpage, "text/html");

                Transport.send(message);

                logger.info("A password reset mail was send to {}", receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    public void sendUserActivationMail(String receipent, String email, String username, Integer id) {
        readInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailAdres));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Gebruiker activeren brandfonds");

                //Readout html page
                String data = "";
                ClassPathResource resource = new ClassPathResource("/html_pages/ActivateUser.html");
                try {
                    byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
                    data = new String(dataArr, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    // do whatever
                }

                String newhtmlpage = data.replace("ACTIVATEUSERURL", activateUserUrl + id);
                newhtmlpage = newhtmlpage.replace("USERNAME", username);
                newhtmlpage = newhtmlpage.replace("MAILADRES", email);

                message.setContent(newhtmlpage, "text/html");

                Transport.send(message);

                logger.info("A activition request mail was send to {}", receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    public void sendUserActivatedMail(String receipent, String email, String username) {
        readInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailAdres));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Gebruiker geactiveerd brandfonds");

                //Readout html page
                String data = "";
                ClassPathResource resource = new ClassPathResource("/html_pages/ActivatedUser.html");
                try {
                    byte[] dataArr = FileCopyUtils.copyToByteArray(resource.getInputStream());
                    data = new String(dataArr, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    // do whatever
                }

                String newhtmlpage = data.replace("USERNAME", username);
                newhtmlpage = newhtmlpage.replace("MAILADRES", email);

                message.setContent(newhtmlpage, "text/html");

                Transport.send(message);

                logger.info("A activition conformation mail was send to {}", receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    private void readInputProperties() {
        mailproperties = new Properties();

        InputStream input = null;

        try {

            input = BrandfondsApplication.class.getResourceAsStream("/mail.properties");

            // load a properties file
            mailproperties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mailSession = Session.getInstance(mailproperties,
                new Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailAdres, mailPassword);
                    }
                });
    }


}
