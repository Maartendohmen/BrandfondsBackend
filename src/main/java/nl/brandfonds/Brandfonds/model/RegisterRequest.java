package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    @Id
    @GeneratedValue
    private Integer id;
    private String randomString;
    private Date initialdate;

    private String emailadres;
    private String forname;
    private String surname;
    private String password;

    public RegisterRequest(String emailadres, String forname, String surname, String password) {
        this.randomString = RandomStringUtils.randomAlphanumeric(15);
        this.initialdate = new Date();
        this.emailadres = emailadres;
        this.forname = forname;
        this.surname = surname;
        this.password = password;
    }
}
