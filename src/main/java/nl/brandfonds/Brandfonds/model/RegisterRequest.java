package nl.brandfonds.Brandfonds.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class RegisterRequest {

    @Id @GeneratedValue
    private Integer id;
    private String randomString;
    private Date initialdate;

    private String emailadres;
    private String forname;
    private String surname;
    private String password;

    public RegisterRequest() {
    }
    public RegisterRequest(String emailadres, String forname, String surname, String password) {
        this.randomString = RandomStringUtils.randomAlphanumeric(15);
        this.initialdate = new Date();
        this.emailadres = emailadres;
        this.forname = forname;
        this.surname = surname;
        this.password = password;
    }

    public String getRandomString() {
        return randomString;
    }
    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }

    public Date getInitialdate() {
        return initialdate;
    }

    public String getEmailadres() {
        return emailadres;
    }
    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public String getForname() {
        return forname;
    }
    public void setForname(String forname) {
        this.forname = forname;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
