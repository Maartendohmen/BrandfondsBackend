package nl.brandfonds.Brandfonds.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer currentBottles;
    private Integer returnedBottles;
    private Integer nonStripedBottles;

}
