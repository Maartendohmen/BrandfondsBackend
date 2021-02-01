package nl.brandfonds.Brandfonds.model;

import nl.brandfonds.Brandfonds.model.files.FileType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Receipt {

    @Id
    @GeneratedValue
    private Integer id;
    private String filename;
    private FileType type;

    private String description;
    private Date paidDate;
    private Long paidAmount;


    public Receipt(){}

    public Receipt(String filename, FileType type, String description, Date paidDate, Long paidAmount) {
        this.filename = filename;
        this.type = type;
        this.description = description;
        this.paidDate = paidDate;
        this.paidAmount = paidAmount;
    }

    public Integer getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileType getType() {
        return type;
    }
    public void setType(FileType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPaidDate() {
        return paidDate;
    }
    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }
    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }
}
