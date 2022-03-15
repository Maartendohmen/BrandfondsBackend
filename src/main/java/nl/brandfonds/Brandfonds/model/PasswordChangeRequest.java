package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class PasswordChangeRequest {

    @Id
    @GeneratedValue
    private Integer id;
    private String mailadres;
    private String randomString;
    private LocalDateTime initialDate;

    public PasswordChangeRequest(String mailadres) {
        this.mailadres = mailadres;
        this.randomString = RandomStringUtils.randomAlphanumeric(10);
        this.initialDate = LocalDateTime.now();
    }
}
