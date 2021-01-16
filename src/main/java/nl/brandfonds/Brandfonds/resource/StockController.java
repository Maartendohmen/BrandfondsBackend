package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/stock")
public class StockController {

    @Autowired
    IStockService stockService;

    @GetMapping
    @ApiOperation(value = "Get current stock", notes = "Gets the current stock")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stock was successfully retrieved", response = Stock.class),
            @ApiResponse(code = 404, message = "The stock values weren't found", response = ResponseEntity.class)
    })
    public Stock getStock() throws NotFoundException {
        if (!stockService.getStock().isPresent()) {
            throw new NotFoundException("De voorraad informatie kan op dit moment niet opgehaald worden, probeer het later opnieuw");
        }
        return stockService.getStock().get();
    }

    @PutMapping(path = "/editcurrentbottles/{amount}")
    @ApiOperation(value = "Update current bottles", notes = "Updates the amount of bottles currently in stock")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Stock was successfully updated", response = Integer.class)
    })
    public int updateCurrentBottles(@PathVariable("amount") Integer amount) {
        return stockService.updateCurrentBottles(amount);
    }

    @PutMapping(path = "/editreturnedbottles/{amount}")
    @ApiOperation(value = "Update returned bottles", notes = "Updates the amount of bottles returned to the store")
    @ApiResponses({
            @ApiResponse(code = 200, message = "returned bottles was successfully updated", response = Integer.class)
    })
    public int updateReturnedBottles(@PathVariable("amount") Integer amount) {
        return stockService.updateReturnedBottles(amount);
    }
}
