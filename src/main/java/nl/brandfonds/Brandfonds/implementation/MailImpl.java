package nl.brandfonds.Brandfonds.implementation;

import nl.brandfonds.Brandfonds.BrandfondsApplication;
import nl.brandfonds.Brandfonds.abstraction.IMailService;
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


    private String EMAILADRESS;
    private String PASSWORD;
    private String FORGOTPASSWORDBASEURL;
    private String REGISTRATIONURL;

    private Properties mailproperties;
    private Session mailSession;

    @Value("${automaticmail.mailadres}")
    public void setMailadres(String mailadres) {
        EMAILADRESS = mailadres;
    }

    @Value("${automaticmail.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    @Value("${resetpassword.ownurl}")
    public void setFORGOTPASSWORDBASEURL(String FORGOTPASSWORDBASEURL) {
        this.FORGOTPASSWORDBASEURL = FORGOTPASSWORDBASEURL;
    }

    @Value("${registration.ownurl}")
    public void setREGISTRATIONURL(String REGISTRATIONURL) {
        this.REGISTRATIONURL = REGISTRATIONURL;
    }



    //todo clean up email text and make it nicer
    //BE AWARE AVG MUST BE OFF BEFORE TESTING

    public void SendRegisterMail(String receipent, String registrationCode) {
        ReadInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(EMAILADRESS));
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

                String newhtmlpage = data.replace("registratielink",REGISTRATIONURL + registrationCode);

                message.setContent(newhtmlpage,"text/html");

                Transport.send(message);

                System.out.println("nl.pts44.pts44backend.MailImpl sent to: " + receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();

    }

    public void SendChangePasswordMail(String receipent, String forgotPasswordCode) {
        ReadInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(EMAILADRESS));
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

                String newhtmlpage = data.replace("resetpasswordlink",FORGOTPASSWORDBASEURL + forgotPasswordCode);

                message.setContent(newhtmlpage,"text/html");

                Transport.send(message);

                System.out.println("nl.pts44.pts44backend.MailImpl sent to: " + receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    private void ReadInputProperties() {
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
                        return new PasswordAuthentication(EMAILADRESS, PASSWORD);
                    }
                });
    }


}
