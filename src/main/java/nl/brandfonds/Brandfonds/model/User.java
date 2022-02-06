package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Column(unique = true)
    private String mailadres;
    private String forename;
    private String surname;
    private String profilePictureFileName;
    @Setter(value = AccessLevel.NONE)
    private String password;
    @Column(columnDefinition = "double")
    private long saldo; //in cents
    private boolean activated;

    public User(String mailadres, String forename, String surname, String password) {
        this.mailadres = mailadres;
        this.forename = forename;
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
