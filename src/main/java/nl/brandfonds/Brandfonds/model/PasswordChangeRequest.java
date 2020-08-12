package nl.brandfonds.Brandfonds.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PasswordChangeRequest {

    @Id @GeneratedValue
    private Integer id;
    private String emailadres;
    private String randomString;
    private Date initialdate;

    public PasswordChangeRequest()
    {

    }

    public PasswordChangeRequest(String emailadres) {
        this.emailadres = emailadres;
        this.randomString = RandomStringUtils.randomAlphanumeric(10);
        this.initialdate = new Date();
    }

    public String getEmailadres() {
        return emailadres;
    }
    public String getRandomstring() {
        return randomString;
    }
    public Date getInitialdate() {return  initialdate;}
}
