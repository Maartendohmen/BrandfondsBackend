package nl.brandfonds.Brandfonds.resource;

import nl.brandfonds.Brandfonds.model.Stock;
import nl.brandfonds.Brandfonds.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/stock")
public class StockControllerImpl implements StockController {

    @Autowired
    StockService stockService;

    @GetMapping
    public Stock getStock() {
        return stockService.getStock();
    }

    @PutMapping(path = "/editCurrentBottles/{amount}")
    public void updateCurrentBottles(@PathVariable("amount") Integer amount) {
        stockService.updateCurrentBottles(amount);
    }

    @PutMapping(path = "/editReturnedBottles/{amount}")
    public void updateReturnedBottles(@PathVariable("amount") Integer amount) {
        stockService.updateReturnedBottles(amount);
    }

    @PutMapping(path = "/editnonstripedbottles/{amount}")
    public void updateNotStripedBottles(@PathVariable("amount") Integer amount) {
        stockService.updateNonStripedBottles(amount);
    }
}
