package nl.brandfonds.Brandfonds.model.util;

import nl.brandfonds.Brandfonds.BrandfondsApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Mail {

        private static final String  mailadres = "blabla";
        private static final String password = "blabla";
        private static Properties mailproperties;
        private static Session mailSession;

        private Mail()
        {

        }

        //todo clean up email text and make it nicer
    //BE AWARE AVG MUST BE OFF BEFORE TESTING

    public static void SendRegisterMail(String receipent, String registrationCode)
    {
        ReadInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailadres));
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

                System.out.println("nl.pts44.pts44backend.Mail sent to: " + receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();

    }

    public static void SendChangePasswordMail(String receipent, String forgotPasswordCode)
    {
        ReadInputProperties();

        Thread t = new Thread(() -> {
            try {
                MimeMessage message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(mailadres));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receipent));
                message.setSubject("Registratie brandfonds");

                message.setText(
                        "Ga naar de volgende pagina om uw wachtwoord te veranderen"
                        +
                                System.lineSeparator()
                        +
                               "http://localhost:4200/resetpassword/" +  forgotPasswordCode
                );
                Transport.send(message);

                System.out.println("nl.pts44.pts44backend.Mail sent to: " + receipent);

            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    private static void ReadInputProperties()
    {
        mailproperties = new Properties();

        InputStream input = null;

        try {

            input = BrandfondsApplication.class.getResourceAsStream("/config.properties");

            // load a properties file
            mailproperties.load(input);
        }
        catch (IOException ex) {
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
                        return new PasswordAuthentication(mailadres, password);
                    }
                });
    }


}
