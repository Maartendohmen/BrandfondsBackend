package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/stock")
public class StockController {

    @Autowired
    IStockService stockService;

    @RequestMapping(method = RequestMethod.GET)
    public Stock GetStock(){
        return stockService.GetStock();
    }

    @RequestMapping(path = "/editcurrentbottles/{amount}",method = RequestMethod.PUT)
    public int UpdateCurrentBottles(@PathVariable("amount") Integer amount){
        return stockService.UpdateCurrentBottles(amount);
    }

    @RequestMapping(path = "/editreturnedbottles/{amount}",method = RequestMethod.PUT)
    public int UpdateReturnedBottles(@PathVariable("amount") Integer amount){
        return stockService.UpdateReturnedBottles(amount);
    }
}
