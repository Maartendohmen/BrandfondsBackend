package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Stock;

import java.util.Optional;

public interface IStockService {

    Optional<Stock> getStock();

    void updateCurrentBottles(Integer amount);

    void updateReturnedBottles(Integer amount);

    void updateNonStripedBottles(Integer amount);

    void addToStock(Integer amount);

    void removeFromStock(Integer amount);
}
