package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Stock;

import java.util.Optional;

public interface IStockService {

    Optional<Stock> getStock();

    int updateCurrentBottles(Integer amount);

    int updateReturnedBottles(Integer amount);

    int updateNonStripedBottles(Integer amount);

    int addToStock(Integer amount);

    int removeFromStock(Integer amount);
}
