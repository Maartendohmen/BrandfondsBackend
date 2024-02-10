package nl.brandfonds.Brandfonds.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Day {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @OneToOne
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    private Integer stripes;

    public Day(User user, LocalDate date, Integer stripes) {
        this.user = user;
        this.date = date;
        this.stripes = stripes;
    }
}
