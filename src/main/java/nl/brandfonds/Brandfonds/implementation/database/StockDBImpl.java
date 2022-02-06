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
    public int updateCurrentBottles(Integer amount) {
        int result = stockRepository.updateCurrentBottles(amount);
        log.info("Current bottles in stock was updated to {}", amount);
        return result;
    }

    @Override
    public int updateReturnedBottles(Integer amount) {
        int result = stockRepository.updateReturnedBottles(amount);
        log.info("Updated bottles in stock was updated to {}", amount);
        return result;
    }

    @Override
    public int updateNonStripedBottles(Integer amount) {
        return stockRepository.updateNonStripedBottles(amount);
    }

    @Override
    public int addToStock(Integer amount) {
        return stockRepository.addMultipleToStock(amount);
    }

    @Override
    public int removeFromStock(Integer amount) {
        return stockRepository.removeMultipleFromStock(amount);
    }
}
