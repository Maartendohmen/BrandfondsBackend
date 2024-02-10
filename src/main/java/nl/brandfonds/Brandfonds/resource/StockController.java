package nl.brandfonds.Brandfonds.resource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.brandfonds.Brandfonds.model.Stock;

@Tag(name = "Stock", description = "Stock operations")
@SecurityRequirement(name = "Bearer_Authentication")
public interface StockController {

    @Operation(summary = "Get current stock", operationId = "getCurrentStock", description = "Gets the current stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock was successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "The stock values weren't found")
    })
    Stock getStock();

    @Operation(summary = "Update current bottles", operationId = "updateCurrentBottles", description = "Updates the amount of bottles currently in stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock was successfully updated")
    })
    void updateCurrentBottles(Integer Amount);

    @Operation(summary = "Update returned bottles", operationId = "updateReturnedBottles", description = "Updates the amount of bottles returned to the store")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "returned bottles was successfully updated")
    })
    void updateReturnedBottles(Integer amount);

    @Operation(summary = "Update non striped bottles", operationId = "updateNonStripedBottles", description = "Updates the amount of bottles that are not striped")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "non striped bottles was successfully updated")
    })
    void updateNotStripedBottles(Integer amount);
}
