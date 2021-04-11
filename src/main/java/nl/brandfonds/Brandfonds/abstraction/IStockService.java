package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Stock;

import java.util.Optional;

public interface IStockService {

    Optional<Stock> getStock();

    int updateCurrentBottles(Integer amount);

    int updateReturnedBottles(Integer amount);

    int updateNonStripedBottles(Integer amount);

    int addOneToStock();

    int removeOneFromStock();

    int addMultipleToStock(Integer amount);

    int removeMultipleFromStock(Integer amount);
}
