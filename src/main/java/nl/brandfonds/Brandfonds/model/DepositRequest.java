package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DepositRequest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @OneToOne
    private User user;
    private Float amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime requestDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime handledDate;
    private boolean accepted;

    public DepositRequest(User user, Float amount) {
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
