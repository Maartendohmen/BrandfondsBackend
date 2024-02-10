package nl.brandfonds.Brandfonds.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
public class PasswordChangeRequest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String email;
    private String randomString;
    private LocalDateTime initialDate;

    public PasswordChangeRequest(String email) {
        this.email = email;
        this.randomString = RandomStringUtils.randomAlphanumeric(10);
        this.initialDate = LocalDateTime.now();
    }
}
