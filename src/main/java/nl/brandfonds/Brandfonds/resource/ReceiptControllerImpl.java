package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IReceiptService;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = "/rest/receipt")
public class ReceiptControllerImpl implements ReceiptController {

    @Autowired
    IReceiptService receiptService;

    @PostMapping
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
    public List<Receipt> retrieveAllReceipt() {
        return receiptService.getAll();
    }

    @GetMapping(path = "/file/encoded/{filename}")
    public String getEncodedReceiptFileByName(@PathVariable("filename") String filename) throws NotFoundException, IOException {
        return receiptService.getEncodedReceiptFileByName(filename);
    }

    @GetMapping(path = "/file/raw/{filename}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getRawReceiptFileByName(@PathVariable("filename") String filename) throws NotFoundException, IOException {
        byte[] image = receiptService.getRawReceiptFileByName(filename);

        var responseEntity = ResponseEntity.ok(image);
        responseEntity.getHeaders().setContentLength(image.length);
        responseEntity.getHeaders().setContentType(new MediaType("image", "jpeg"));
        responseEntity.getHeaders().setCacheControl("must-revalidate, post-check=0, pre-check=0");
        responseEntity.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return responseEntity;
    }
}


