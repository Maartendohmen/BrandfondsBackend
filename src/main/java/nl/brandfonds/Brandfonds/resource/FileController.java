package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import nl.brandfonds.Brandfonds.model.responses.ReceiptFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/file")
public class FileController {

    @Autowired
    IReceiptService receiptService;

    @PostMapping(path = "/receipt")
    @ApiOperation(value = "Upload a receipt file", notes = "Upload a receipt file to save", nickname = "uploadReceiptFile")
    public void uploadStockReceipt(@RequestParam("file") MultipartFile file,
                                   @RequestParam("description") String description,
                                   @RequestParam(name = "paidAmount",required = false) Long paidAmount) throws IOException, AlreadyExistException {
        receiptService.saveFile(file, description, paidAmount);
    }

    @GetMapping(path = "/receipt")
    @ApiOperation(value = "Retrieve all receipts",notes = "Retrieve all receipts info", nickname = "getAllReceipts")
    public List<Receipt> retrieveAllReceipt() {
        return receiptService.getAll();
    }

    @GetMapping(path = "/receipt/{filename}")
    @ApiOperation(value = "Retrieve a receipt file with a filename", notes = "Retrieve receipt info with file using filename", nickname = "getReceiptFileByName")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt was succesfully loaded", response = ReceiptFile.class)
    })
    public ReceiptFile getReceiptFileByName(@PathVariable("filename") String filename) throws NotFoundException, IOException {
        return receiptService.getReceiptFileByName(filename);
    }


}
