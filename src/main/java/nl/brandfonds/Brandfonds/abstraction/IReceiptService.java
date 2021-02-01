package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import nl.brandfonds.Brandfonds.model.responses.ReceiptFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


public interface IReceiptService {

    void saveFile(MultipartFile file, String description, Long paidAmount) throws IOException, AlreadyExistException;

    void delete(Receipt receipt);

    List<Receipt> getAll();

    Receipt getReceiptByName(String name) throws NotFoundException;

    ReceiptFile getReceiptFileByName(String name) throws NotFoundException, IOException;

}
