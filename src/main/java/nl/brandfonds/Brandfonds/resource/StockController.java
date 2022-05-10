package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.*;
import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.http.ResponseEntity;

@Api(tags = "Stock", description = "Stock operations")
public interface StockController {

    @ApiOperation(value = "Get current stock", nickname = "getCurrentStock", notes = "Gets the current stock", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stock was successfully retrieved", response = Stock.class),
            @ApiResponse(code = 404, message = "The stock values weren't found", response = ResponseEntity.class)
    })
    Stock getStock();

    @ApiOperation(value = "Update current bottles", nickname = "updateCurrentBottles", notes = "Updates the amount of bottles currently in stock", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stock was successfully updated", response = Integer.class)
    })
    void updateCurrentBottles(Integer Amount);

    @ApiOperation(value = "Update returned bottles", nickname = "updateReturnedBottles", notes = "Updates the amount of bottles returned to the store", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "returned bottles was successfully updated", response = Integer.class)
    })
    void updateReturnedBottles(Integer amount);

    @ApiOperation(value = "Update non striped bottles", nickname = "updateNonStripedBottles", notes = "Updates the amount of bottles that are not striped", authorizations = @Authorization(value = "jwtToken"))
    @ApiResponses({
            @ApiResponse(code = 200, message = "non striped bottles was successfully updated", response = Integer.class)
    })
    void updateNotStripedBottles(Integer amount);
}
