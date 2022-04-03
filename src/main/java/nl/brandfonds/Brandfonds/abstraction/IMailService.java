package nl.brandfonds.Brandfonds.abstraction;

public interface IMailService {

    void sendRegisterMail(String recipient, String registrationCode);

    void sendChangePasswordMail(String recipient, String forgotPasswordCode);

    void sendUserActivationMail(String recipient, String email, String username, Integer id);

    void sendUserActivatedMail(String recipient, String email, String username);
}
