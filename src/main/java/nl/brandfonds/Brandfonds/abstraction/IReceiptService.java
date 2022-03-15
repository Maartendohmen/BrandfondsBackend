package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public interface IReceiptService {

    void saveReceiptFile(MultipartFile file, String description, LocalDate paidDate, Long paidAmount) throws IOException, AlreadyExistException;

    void delete(Receipt receipt);

    List<Receipt> getAll();

    Receipt getReceiptByName(String name) throws NotFoundException;

    String getEncodedReceiptFileByName(String name) throws NotFoundException, IOException;

    byte[] getRawReceiptFileByName(String name) throws NotFoundException, IOException;
}
