package nl.brandfonds.Brandfonds.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PasswordChangeRequest {

    @Id @GeneratedValue
    private Integer id;
    private String emailadres;
    private String randomString;

    public PasswordChangeRequest()
    {

    }

    public PasswordChangeRequest(String emailadres) {
        this.emailadres = emailadres;
        this.randomString = RandomStringUtils.randomAlphanumeric(10);
    }

    public String getEmailadres() {
        return emailadres;
    }
    public String getRandomstring() {
        return randomString;
    }
}
