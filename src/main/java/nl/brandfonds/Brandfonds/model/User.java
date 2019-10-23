package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.brandfonds.Brandfonds.model.util.SHA256;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String emailadres;
    private String username;
    private String password;

    public User() {
    }
    public User(String username) {
        this.username = username;
    }
    public User(String emailadres,String username, String password) {
        this.emailadres = emailadres;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getEmailadres() {
        return emailadres;
    }
    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = SHA256.SHA256(password);
    }
}
