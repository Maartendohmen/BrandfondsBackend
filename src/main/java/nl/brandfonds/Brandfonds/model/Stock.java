package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer currentBottles;
    private Integer returnedBottles;
    private Integer nonStripedBottles;

}
