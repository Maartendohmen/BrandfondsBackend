package nl.brandfonds.Brandfonds.security;

public class AuthenticationRequest {

    private String mailadres;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String mailadres, String password) {
        this.mailadres = mailadres;
        this.password = password;
    }

    public String getMailadres() {
        return mailadres;
    }

    public void setMailadres(String mailadres) {
        this.mailadres = mailadres;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
