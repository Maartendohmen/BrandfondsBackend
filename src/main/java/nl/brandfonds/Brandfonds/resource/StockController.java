package nl.brandfonds.Brandfonds.resource;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/stock")
public class StockController {

    @Autowired
    IStockService stockService;

    @RequestMapping(method = RequestMethod.GET)
    public Stock GetStock(){
        return stockService.GetStock();
    }

    @PutMapping(path = "/editcurrentbottles/{amount}")
    @ApiOperation(value = "Update current bottles", notes = "Updates the amount of bottles currently in stock")
    @ApiResponses({
            @ApiResponse(code= 200,message = "Stock was successfully updated")
    })
    public int UpdateCurrentBottles(@PathVariable("amount") Integer amount){
        return stockService.UpdateCurrentBottles(amount);
    }

    @PutMapping(path = "/editreturnedbottles/{amount}")
    @ApiOperation(value = "Update returned bottles", notes = "Updates the amount of bottles returned to the store")
    @ApiResponses({
            @ApiResponse(code= 200,message = "returned bottles was successfully updated")
    })
    public int UpdateReturnedBottles(@PathVariable("amount") Integer amount){
        return stockService.UpdateReturnedBottles(amount);
    }
}
