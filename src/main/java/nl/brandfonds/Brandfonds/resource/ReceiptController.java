package nl.brandfonds.Brandfonds.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.brandfonds.Brandfonds.exceptions.AlreadyExistException;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Receipt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "Receipt", description = "Receipt operations")
@SecurityRequirement(name = "Bearer_Authentication")
public interface ReceiptController {

    @Operation(summary = "Upload a receipt file", operationId = "uploadReceiptFile", description = "Upload a receipt file to save")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receipt file was successfully uploaded"),
            @ApiResponse(responseCode = "409", description = "File already exists on disk"),
    })
    void uploadReceipt(MultipartFile file, String description, LocalDate paidDate, Float paidAmount) throws IOException, AlreadyExistException;

    @Operation(summary = "Retrieve all receipts", operationId = "retrieveAllReceipts", description = "Retrieve all receipts info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receipt files were successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Receipt.class)))),
    })
    List<Receipt> retrieveAllReceipt();

    @Operation(summary = "Retrieve raw receipt image", operationId = "retrieveRawReceiptImage", description = "Retrieve the raw receipt image as bytes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receipt file was successfully loaded"),
            @ApiResponse(responseCode = "404", description = "Receipt file with filename was not found")
    })
    byte[] getReceiptFileByName(String filename) throws NotFoundException, IOException;
}
