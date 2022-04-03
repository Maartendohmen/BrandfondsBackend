package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Api(tags = "Receipt", description = "Receipt operations")
public interface ReceiptController {

    @ApiOperation(value = "Upload a receipt file", nickname = "uploadReceiptFile", notes = "Upload a receipt file to save")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt file was successfully uploaded", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "File already exists on disk", response = ResponseEntity.class),
    })
    void uploadReceipt(MultipartFile file, String description, LocalDate paidDate, Long paidAmount) throws IOException, AlreadyExistException;

    @ApiOperation(value = "Retrieve all receipts", nickname = "retrieveAllReceipts", notes = "Retrieve all receipts info")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt files were successfully retrieved", response = Receipt.class, responseContainer = "List"),
    })
    List<Receipt> retrieveAllReceipt();

    @ApiOperation(value = "Retrieve encoded receipt image", nickname = "retrieveEncodedReceiptImage", notes = "Retrieve the encoded receipt image as string", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt file was successfully loaded", response = String.class),
            @ApiResponse(code = 404, message = "Receipt file with filename was not found", response = ResponseEntity.class)
    })
    String getEncodedReceiptFileByName(String filename) throws NotFoundException, IOException;

    @ApiOperation(value = "Retrieve raw receipt image", nickname = "retrieveRawReceiptImage", notes = "Retrieve the raw receipt image as bytes", responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Receipt file was successfully loaded"),
            @ApiResponse(code = 404, message = "Receipt file with filename was not found", response = ResponseEntity.class)
    })
    ResponseEntity<byte[]> getRawReceiptFileByName(String filename) throws NotFoundException, IOException;
}
