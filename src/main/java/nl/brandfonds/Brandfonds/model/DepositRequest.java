package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DepositRequest {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private User user;
    private long amount; //in cents;
    private LocalDateTime requestDate;
    private LocalDateTime handledDate;
    private boolean accepted;

    public DepositRequest(User user, long amount) {
        this.user = user;
        this.amount = amount;
        this.requestDate = LocalDateTime.now();
        this.handledDate = null;
        this.accepted = false;
    }

    public void ValidateRequest() {
        this.handledDate = LocalDateTime.now();
        this.accepted = true;
    }
}
