package nl.brandfonds.Brandfonds.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RegisterRequest {

    @Id @GeneratedValue
    private Integer id;
    private String randomString;

    private String emailadres;
    private String username;
    private String password;

    public RegisterRequest() {
    }
    public RegisterRequest(String emailadres, String username, String password) {
        this.randomString = RandomStringUtils.randomAlphanumeric(15);
        this.emailadres = emailadres;
        this.username = username;
        this.password = password;
    }

    public String getRandomString() {
        return randomString;
    }
    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }
    public String getEmailadres() {
        return emailadres;
    }
    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
