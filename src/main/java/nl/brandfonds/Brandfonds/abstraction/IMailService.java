package nl.brandfonds.Brandfonds.abstraction;

public interface IMailService {

    public abstract void SendRegisterMail(String receipent, String registrationCode);

    public abstract void SendChangePasswordMail(String receipent, String forgotPasswordCode);
}
