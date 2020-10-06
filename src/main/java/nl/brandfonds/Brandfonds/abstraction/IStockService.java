package nl.brandfonds.Brandfonds.abstraction;

import nl.brandfonds.Brandfonds.model.Stock;

public interface IStockService {

    public abstract Stock GetStock();

    public abstract int UpdateCurrentBottles(Integer amount);

    public abstract int UpdateReturnedBottles(Integer amount);

    public abstract int AddOneToStock();

    public abstract int RemoveOneFromStock();

    public abstract int AddMultipleToStock(Integer amount);

    public abstract int RemoveMultipleFromStock(Integer amount);
}
