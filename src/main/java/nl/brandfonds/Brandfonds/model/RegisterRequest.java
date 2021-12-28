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
    private Date initialDate;

    private String mailadres;
    private String forename;
    private String surname;
    private String password;

    public RegisterRequest(String mailadres, String forename, String surname, String password) {
        this.randomString = RandomStringUtils.randomAlphanumeric(15);
        this.initialDate = new Date();
        this.mailadres = mailadres;
        this.forename = forename;
        this.surname = surname;
        this.password = password;
    }
}
