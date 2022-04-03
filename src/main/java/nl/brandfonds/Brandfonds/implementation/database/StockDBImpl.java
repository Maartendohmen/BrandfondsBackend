package nl.brandfonds.Brandfonds.implementation.database;

import lombok.extern.slf4j.Slf4j;
import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.model.Stock;
import nl.brandfonds.Brandfonds.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StockDBImpl implements IStockService {

    @Autowired
    StockRepository stockRepository;

    @Override
    public Optional<Stock> getStock() {
        return stockRepository.getStock();
    }

    @Override
    public void updateCurrentBottles(Integer amount) {
        stockRepository.updateCurrentBottles(amount);
        log.info("Current bottles in stock was updated to {}", amount);

    }

    @Override
    public void updateReturnedBottles(Integer amount) {
        stockRepository.updateReturnedBottles(amount);
        log.info("Updated bottles in stock was updated to {}", amount);
    }

    @Override
    public void updateNonStripedBottles(Integer amount) {
        stockRepository.updateNonStripedBottles(amount);
    }

    @Override
    public void addToStock(Integer amount) {
        stockRepository.addMultipleToStock(amount);
    }

    @Override
    public void removeFromStock(Integer amount) {
        stockRepository.removeMultipleFromStock(amount);
    }
}
