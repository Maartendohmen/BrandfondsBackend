package nl.brandfonds.Brandfonds.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DepositRequest {

    @Id @GeneratedValue
    private Integer id;
    @OneToOne
    private User user;
    private int amount; //in cents;
    private Date requestDate;
    private Date handledDate;
    private boolean accepted;

    public DepositRequest() {
    }

    public DepositRequest(User user, int amount) {
        this.user = user;
        this.amount = amount;
        this.requestDate = new Date();
        this.handledDate = null;
        this.accepted = false;
    }

    public Integer getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public int getAmount() {
        return amount;
    }
    public Date getRequestDate() {
        return requestDate;
    }
    public Date getHandledDate() {
        return handledDate;
    }
    public boolean isAccepted() {
        return accepted;
    }

    //Setters to comply to JPA
    public void setId(Integer id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public void setHandledDate(Date handledDate) {
        this.handledDate = handledDate;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void ValidateRequest()
    {
        this.handledDate = new Date();
        this.accepted = true;
    }
}
