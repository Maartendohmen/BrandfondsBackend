package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.brandfonds.Brandfonds.model.util.SHA256;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String emailadres;
    private String forname;
    private String surname;
    private String password;
    @Column(columnDefinition="double")
    private long saldo; //in cents

    public User() {
    }
    public User(String emailadres,String forname, String surname, String password) {
        this.emailadres = emailadres;
        this.forname = forname;
        this.surname = surname;
        this.password = password;
        this.saldo = 0;
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

    public String getForname() {
        return forname;
    }
    public void setForname(String forname) {
        this.forname = forname;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = SHA256.SHA256(password);
    }

    public long getSaldo() {
        return saldo;
    }
    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }
}
