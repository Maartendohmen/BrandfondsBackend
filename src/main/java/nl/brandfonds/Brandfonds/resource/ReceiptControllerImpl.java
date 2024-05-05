package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import nl.brandfonds.Brandfonds.services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/receipt")
public class ReceiptControllerImpl implements ReceiptController {

    @Autowired
    ReceiptService receiptService;

    @PostMapping
    public void uploadReceipt(@RequestParam("file") MultipartFile file,
                              @RequestParam("description") String description,
                              @RequestParam(value = "date", required = false) LocalDate paidDate,
                              @RequestParam(name = "paidAmount", required = false) Float paidAmount) throws IOException, AlreadyExistException {

        if (paidDate == null) {
            paidDate = LocalDate.now();
        }

        receiptService.saveReceiptFile(file, description, paidDate, paidAmount);
    }

    @GetMapping
    public List<Receipt> retrieveAllReceipt() {
        return receiptService.getAll();
    }

    @GetMapping(path = "/image/{receiptId}", produces = "image/jpeg")
    public byte[] getReceiptFileByName(@PathVariable("receiptId") Integer receiptId) throws NotFoundException, IOException {
        return receiptService.getRawReceiptFileByName(receiptId);
    }
}


