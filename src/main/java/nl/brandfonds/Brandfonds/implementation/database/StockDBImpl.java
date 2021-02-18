package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.model.Stock;
import nl.brandfonds.Brandfonds.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockDBImpl implements IStockService {

    private static final Logger logger = LoggerFactory.getLogger(StockDBImpl.class);

    @Autowired
    StockRepository stockRepository;


    @Override
    public Optional<Stock> getStock() {
        return stockRepository.getStock();
    }

    @Override
    public int updateCurrentBottles(Integer amount) {
        int result = stockRepository.updateCurrentBottles(amount);
        logger.info("Current bottles in stock was updated to {}", amount);
        return result;
    }

    @Override
    public int updateReturnedBottles(Integer amount) {
        int result = stockRepository.updateReturnedBottles(amount);
        logger.info("Updated bottles in stock was updated to {}", amount);
        return result;
    }

    @Override
    public int addOneToStock() {
        return stockRepository.addOneToStock();
    }

    @Override
    public int removeOneFromStock() {
        return stockRepository.removeOneFromStock();
    }

    @Override
    public int addMultipleToStock(Integer amount) {
        return stockRepository.addMultipleToStock(amount);
    }

    @Override
    public int removeMultipleFromStock(Integer amount) {
        return stockRepository.removeMultipleFromStock(amount);
    }
}
