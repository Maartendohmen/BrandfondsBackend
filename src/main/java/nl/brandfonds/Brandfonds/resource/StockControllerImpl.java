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

    @PutMapping
    public void updateStock(@RequestBody Stock updatedStock) {
        stockService.updateStock(updatedStock);
    }
}
