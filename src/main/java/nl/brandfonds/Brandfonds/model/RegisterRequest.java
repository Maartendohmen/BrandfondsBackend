package nl.brandfonds.Brandfonds.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String randomString;
    private LocalDateTime initialDate;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

}
