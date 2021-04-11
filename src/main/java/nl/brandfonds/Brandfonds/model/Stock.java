package nl.brandfonds.Brandfonds.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stock {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer currentBottles;
    private Integer returnedBottles;
    private Integer nonStripedBottles;

    public Stock() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrentBottles() {
        return currentBottles;
    }

    public void setCurrentBottles(Integer currentBottles) {
        this.currentBottles = currentBottles;
    }

    public Integer getReturnedBottles() {
        return returnedBottles;
    }

    public void setReturnedBottles(Integer returnedBottles) {
        this.returnedBottles = returnedBottles;
    }

    public Integer getNonStripedBottles() {
        return nonStripedBottles;
    }

    public void setNonStripedBottles(Integer nonStripedBottles) {
        this.nonStripedBottles = nonStripedBottles;
    }
}
