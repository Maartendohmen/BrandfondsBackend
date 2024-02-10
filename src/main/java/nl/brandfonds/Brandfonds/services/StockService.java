package nl.brandfonds.Brandfonds.services;

import lombok.RequiredArgsConstructor;
import nl.brandfonds.Brandfonds.exceptions.NotFoundException;
import nl.brandfonds.Brandfonds.model.Stock;
import nl.brandfonds.Brandfonds.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public Stock getStock() {
        return stockRepository.findAll().stream().findFirst().orElseThrow(NotFoundException.StockNotFoundException::new);
    }

    public void updateCurrentBottles(Integer amount) {
        var stock = getStock();
        stock.setCurrentBottles(stock.getCurrentBottles() + amount);
        stockRepository.save(stock);
    }

    public void updateReturnedBottles(Integer amount) {
        var stock = getStock();
        stock.setReturnedBottles(stock.getCurrentBottles() + amount);
        stockRepository.save(stock);
    }

    public void updateNonStripedBottles(Integer amount) {
        var stock = getStock();
        stock.setNonStripedBottles(stock.getNonStripedBottles() + amount);
        stockRepository.save(stock);
    }


}
