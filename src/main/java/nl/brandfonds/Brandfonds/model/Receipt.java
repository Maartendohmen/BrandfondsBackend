package nl.brandfonds.Brandfonds.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String fileName;
    private String description;
    private LocalDate paidDate;
    private Float paidAmount;

    public Receipt(String filename, String description, LocalDate paidDate, Float paidAmount) {
        this.fileName = filename;
        this.description = description;
        this.paidDate = paidDate;
        this.paidAmount = paidAmount;
    }
}
