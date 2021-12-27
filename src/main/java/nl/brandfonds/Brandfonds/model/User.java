package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;

@Entity
@NoArgsConstructor
@Getter
@Setter()
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(unique = true)
    private String emailadres;
    private String forname;
    private String surname;
    private String profilePictureFileName;
    @Setter(value = AccessLevel.NONE)
    private String password;
    @Column(columnDefinition = "double")
    private long saldo; //in cents
    private boolean activated;

    public User(String emailadres, String forname, String surname, String password) {
        this.emailadres = emailadres;
        this.forname = forname;
        this.surname = surname;
        this.password = password;
        this.saldo = 0;
        this.userRole = UserRole.NORMAL;
        this.activated = false;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
