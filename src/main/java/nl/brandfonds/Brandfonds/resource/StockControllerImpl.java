package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/stock")
public class StockControllerImpl implements StockController {

    @Autowired
    IStockService stockService;

    @GetMapping
    public Stock getStock() {
        return stockService.getStock().orElseThrow(NotFoundException.StockNotFoundException::new);
    }

    @PutMapping(path = "/editcurrentbottles/{amount}")
    public void updateCurrentBottles(@PathVariable("amount") Integer amount) {
        stockService.getStock().orElseThrow(NotFoundException.StockNotFoundException::new);
        stockService.updateCurrentBottles(amount);
    }

    @PutMapping(path = "/editreturnedbottles/{amount}")
    public void updateReturnedBottles(@PathVariable("amount") Integer amount) {
        stockService.getStock().orElseThrow(NotFoundException.StockNotFoundException::new);
        stockService.updateReturnedBottles(amount);
    }

    @PutMapping(path = "/editnonstripedbottles/{amount}")
    public void updateNotStripedBottles(@PathVariable("amount") Integer amount) {
        stockService.getStock().orElseThrow(NotFoundException.StockNotFoundException::new);
        stockService.updateNonStripedBottles(amount);
    }
}
