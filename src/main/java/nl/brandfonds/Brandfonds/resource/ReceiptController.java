package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/receipt")
public class ReceiptController {

    @Autowired
    IReceiptService receiptService;

    @PostMapping
    @ApiOperation(value = "Upload a receipt file", notes = "Upload a receipt file to save", nickname = "uploadReceiptFile")
    public void uploadReceipt(@RequestParam("file") MultipartFile file,
                              @RequestParam("description") String description,
                              @RequestParam(value = "date", required = false) LocalDate paidDate,
                              @RequestParam(name = "paidAmount", required = false) Long paidAmount) throws IOException, AlreadyExistException {

        if (paidDate == null) {
            paidDate = LocalDate.now();
        }

        receiptService.saveReceiptFile(file, description, paidDate, paidAmount);
    }

    @GetMapping
    @ApiOperation(value = "Retrieve all receipts", notes = "Retrieve all receipts info", nickname = "getAllReceipts")
    public List<Receipt> retrieveAllReceipt() {
        return receiptService.getAll();
    }

    @GetMapping(path = "/file/encoded/{filename}")
    @ApiOperation(value = "Retrieve encoded receipt image", notes = "Retrieve the encoded receipt image as string", nickname = "getEncodedReceiptFileByName")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt file was succesfully loaded")
    })
    public String getEncodedReceiptFileByName(@PathVariable("filename") String filename) throws NotFoundException, IOException {
        return receiptService.getEncodedReceiptFileByName(filename);
    }

    @GetMapping(path = "/file/raw/{filename}", produces = "image/jpeg")
    @ApiOperation(value = "Retrieve raw receipt image", notes = "Retrieve the raw receipt image as bytes", nickname = "getRawReceiptFileByName")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt file was succesfully loaded")
    })
    public ResponseEntity<byte[]> getRawReceiptFileByName(@PathVariable("filename") String filename) throws NotFoundException, IOException {
        byte[] image = receiptService.getRawReceiptFileByName(filename);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(image.length);
        respHeaders.setContentType(new MediaType("image", "jpeg"));
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return new ResponseEntity<byte[]>(image, respHeaders, HttpStatus.OK);
    }
}


