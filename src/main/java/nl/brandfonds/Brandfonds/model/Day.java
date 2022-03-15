package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Day {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private User user;
    private LocalDate date;
    private Integer stripes;

    public Day(User user, LocalDate date, Integer stripes) {
        this.user = user;
        this.date = date;
        this.stripes = stripes;
    }
}
