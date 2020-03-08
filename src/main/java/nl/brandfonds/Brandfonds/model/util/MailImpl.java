package nl.brandfonds.Brandfonds.model.util;

import nl.brandfonds.Brandfonds.BrandfondsApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class MailImpl implements MailService {


    private String EMAILADRESS;
    private String PASSWORD;
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

                message.setText(
                        "Bedankt voor je registratie bij het brandfonds. Kopieer de onderstaande code om je registratie te bevestigen"
                                +
                                System.lineSeparator()
                                +
                                System.lineSeparator()
                                +
                                "De code voor de registratie is : " + registrationCode
                );
                Transport.send(message);

                System.out.println("nl.pts44.pts44backend.MailImpl sent to: " + receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();

    }

    //fix correct mailing
    public void SendChangePasswordMail(String receipent, String forgotPasswordCode) {
        ReadInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(EMAILADRESS));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Wachtwoord reset brandfonds");

                message.setText(
                        "Ga naar de volgende pagina om uw wachtwoord te veranderen"
                                +
                                System.lineSeparator()
                                +
                                "http://localhost:4200/resetpassword/" + forgotPasswordCode
                );
                Transport.send(message);

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
