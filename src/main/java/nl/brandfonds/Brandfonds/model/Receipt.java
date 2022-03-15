package nl.brandfonds.Brandfonds.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue
    private Integer id;
    private String fileName;

    private String description;
    private LocalDate paidDate;
    private long paidAmount;

    public Receipt(String filename, String description, LocalDate paidDate, long paidAmount) {
        this.fileName = filename;
        this.description = description;
        this.paidDate = paidDate;
        this.paidAmount = paidAmount;
    }
}
