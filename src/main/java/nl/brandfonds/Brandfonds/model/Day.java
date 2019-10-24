package nl.brandfonds.Brandfonds.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Day {

    @Id @GeneratedValue
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    private Date date;
    private Integer stripes;

    public Day() {
    }

    public Day(User user, Date date, Integer stripes) {
        this.user = user;
        this.date = date;
        this.stripes = stripes;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStripes() {
        return stripes;
    }
    public void setStripes(Integer stripes) {
        this.stripes = stripes;
    }
}
