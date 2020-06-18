package nl.brandfonds.Brandfonds.abstraction;

public interface IMailService {

    public abstract void SendRegisterMail(String receipent, String registrationCode);

    public abstract void SendChangePasswordMail(String receipent, String forgotPasswordCode);

    public abstract void SendUserActivationMail(String receipent,String email, String username, Integer id);

    public abstract void SendUserActivatedMail(String receipent,String email, String username);
}
