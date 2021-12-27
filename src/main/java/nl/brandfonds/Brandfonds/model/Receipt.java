package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue
    private Integer id;
    private String filename;

    private String description;
    private Date paidDate;
    private Long paidAmount;

    public Receipt(String filename, String description, Date paidDate, Long paidAmount) {
        this.filename = filename;
        this.description = description;
        this.paidDate = paidDate;
        this.paidAmount = paidAmount;
    }
}
