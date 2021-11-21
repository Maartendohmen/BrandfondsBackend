package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
public class PasswordChangeRequest {

    @Id
    @GeneratedValue
    private Integer id;
    private String emailadres;
    private String randomString;
    private Date initialdate;

    public PasswordChangeRequest(String emailadres) {
        this.emailadres = emailadres;
        this.randomString = RandomStringUtils.randomAlphanumeric(10);
        this.initialdate = new Date();
    }
}
