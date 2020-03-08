package nl.brandfonds.Brandfonds.model.util;

public interface MailService {

    public abstract void SendRegisterMail(String receipent, String registrationCode);

    public abstract void SendChangePasswordMail(String receipent, String forgotPasswordCode);
}
