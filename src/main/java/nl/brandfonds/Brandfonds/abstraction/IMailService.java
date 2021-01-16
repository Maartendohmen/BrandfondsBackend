package nl.brandfonds.Brandfonds.abstraction;

public interface IMailService {

    void sendRegisterMail(String receipent, String registrationCode);

    void sendChangePasswordMail(String receipent, String forgotPasswordCode);

    void sendUserActivationMail(String receipent, String email, String username, Integer id);

    void sendUserActivatedMail(String receipent, String email, String username);
}
