package nl.brandfonds.Brandfonds.implementation.database;

import nl.brandfonds.Brandfonds.abstraction.IStockService;
import nl.brandfonds.Brandfonds.model.Stock;
import nl.brandfonds.Brandfonds.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockDBImpl implements IStockService {

    @Autowired
    StockRepository stockRepository;


    @Override
    public Stock GetStock() {
        return stockRepository.GetStock();
    }

    @Override
    public int UpdateCurrentBottles(Integer amount) {
        return stockRepository.UpdateCurrentBottles(amount);
    }

    @Override
    public int UpdateReturnedBottles(Integer amount) {
        return stockRepository.UpdateReturnedBottles(amount);
    }

    @Override
    public int AddOneToStock() {
        return stockRepository.AddOneToStock();
    }

    @Override
    public int RemoveOneFromStock() {
        return stockRepository.RemoveOneFromStock();
    }

    @Override
    public int AddMultipleToStock(Integer amount) {
        return stockRepository.AddMultipleToStock(amount);
    }

    @Override
    public int RemoveMultipleFromStock(Integer amount) {
        return stockRepository.RemoveMultipleFromStock(amount);
    }
}
